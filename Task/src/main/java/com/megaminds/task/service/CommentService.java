package com.megaminds.task.service;

import com.megaminds.task.dto.CommentRequest;
import com.megaminds.task.dto.CommentResponse;
import com.megaminds.task.entity.Comment;
import com.megaminds.task.entity.Task;
import com.megaminds.task.entity.User;
import com.megaminds.task.repository.CommentRepository;
import com.megaminds.task.repository.TaskRepository;
import com.megaminds.task.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.megaminds.task.exception.ResourceNotFoundException;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;


    private static final Integer PROJECT_MANAGER_ID = 4;
    private static final Integer WORKER_ID = 3;

    @Transactional
    public CommentResponse createComment(CommentRequest request, Integer authorId) {
        // Validate author is either PM (3) or Worker (4)
        if (!authorId.equals(PROJECT_MANAGER_ID) && !authorId.equals(WORKER_ID)) {
            throw new IllegalArgumentException("Only users with ID 3 or 4 can comment");
        }

        Task task = taskRepository.findById(request.getTaskId())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        // Verify task is assigned to the worker (ID 4)
        if (!task.getAssignedTo().getId().equals(WORKER_ID)) {
            throw new IllegalArgumentException("Comments only allowed on tasks assigned to worker ID 4");
        }

        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Comment comment = Comment.builder()
                .text(request.getText())
                .task(task)
                .author(author)
                .build();

        comment = commentRepository.save(comment);
        CommentResponse response = mapToResponse(comment);

        // ✅ Real-time broadcast to clients subscribed to the task
        messagingTemplate.convertAndSend(
                "/topic/tasks/" + task.getId() + "/comments",
                response
        );

        return response;    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentsForTask(Integer taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        // Verify task is assigned to the worker (ID 4)
        if (!task.getAssignedTo().getId().equals(WORKER_ID)) {
            throw new IllegalArgumentException("Can only view comments for tasks assigned to worker ID 4");
        }
        return commentRepository.findByTaskIdOrderByCreatedAtAsc(taskId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private CommentResponse mapToResponse(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .text(comment.getText())
                .createdAt(comment.getCreatedAt())
                .authorId(comment.getAuthor().getId())
                .authorName(comment.getAuthor().getUsername())
                .taskId(comment.getTask().getId())
                .build();
    }

    public CommentResponse updateComment(Long commentId, CommentRequest request) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        comment.setText(request.getText());

        Comment updated = commentRepository.save(comment);
        return CommentResponse.fromEntity(updated);
    }

    public void deleteComment(Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new RuntimeException("Comment not found");
        }
        commentRepository.deleteById(commentId);
    }
}
