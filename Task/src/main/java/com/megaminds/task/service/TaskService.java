package com.megaminds.task.service;

import com.megaminds.task.dto.NotificationDTO;
import com.megaminds.task.dto.TaskAssignmentRequest;
import com.megaminds.task.dto.TaskAssignmentResponse;
import com.megaminds.task.dto.TaskRequest;
import com.megaminds.task.dto.TaskResponse;
import com.megaminds.task.entity.Task;
import com.megaminds.task.entity.TaskStatus;
import com.megaminds.task.entity.User;
import com.megaminds.task.exception.TaskException;
import com.megaminds.task.repository.TaskRepository;
import com.megaminds.task.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;
    private final SimpMessagingTemplate messagingTemplate;

    private static final Integer PROJECT_MANAGER_ID = 4; // Static Project Manager ID
    private static final Integer STATIC_WORKER_ID = 3; // Static Worker ID

    public TaskResponse createTask(TaskRequest request, int userId) {
        if (userId != PROJECT_MANAGER_ID) {
            throw new RuntimeException("Only the Project Manager can assign tasks.");
        }

        User assignedWorker = userRepository.findById(request.assignedToId())
                .orElseThrow(() -> new RuntimeException("Invalid Ouvrier ID"));

        Task task = taskMapper.toTask(request, assignedWorker);
        taskRepository.save(task);

        notifyIfSevenOrMorePendingTasks();

        return taskMapper.toTaskResponse(task);
    }

    public TaskAssignmentResponse assignTask(TaskAssignmentRequest request) {
        Task task = taskRepository.findById(request.taskId())
                .orElseThrow(() -> new TaskException("Task not found"));
        User assignedTo = userRepository.findById(request.assignedToId())
                .orElseThrow(() -> new TaskException("Ouvrier not found"));

        task.setAssignedTo(assignedTo);
        taskRepository.save(task);

        return new TaskAssignmentResponse(
                task.getId(),
                assignedTo.getUsername(),
                "Task assigned successfully"
        );
    }

    public TaskResponse getTaskById(Integer taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskException("Task not found"));
        return taskMapper.toTaskResponse(task);
    }

    public List<TaskResponse> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(taskMapper::toTaskResponse)
                .collect(Collectors.toList());
    }

    public TaskResponse updateTask(Integer taskId, TaskRequest request) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskException("Task not found"));

        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setStatus(TaskStatus.valueOf(request.status()));
        task.setDueDate(request.dueDate());

        taskRepository.save(task);

        notifyIfSevenOrMorePendingTasks();

        return taskMapper.toTaskResponse(task);
    }

    @Transactional
    public void deleteTask(Integer taskId, int userId) {
        System.out.println("Trying to delete task with ID: " + taskId);

        if (userId != PROJECT_MANAGER_ID) {
            throw new RuntimeException("Only the Project Manager can delete tasks.");
        }
        if (!taskRepository.existsById(taskId)) {
            throw new TaskException("Task not found");
        }
        System.out.println("Task with ID " + taskId + " exists. Proceeding with delete.");

        taskRepository.deleteById(taskId);
        //taskRepository.flush();
        System.out.println("Task with ID " + taskId + " has been deleted.");

    }

    public List<TaskResponse> getTasksByWorker() {
        return taskRepository.findByAssignedToId(STATIC_WORKER_ID)
                .stream()
                .map(taskMapper::toTaskResponse)
                .collect(Collectors.toList());
    }

    public List<TaskResponse> getAllTasksByUser(int id) {
        return taskRepository.findByAssignedToId(id)
                .stream()
                .map(taskMapper::toTaskResponse)
                .collect(Collectors.toList());
    }

    // 🔔 Notification Logic
    private void notifyIfSevenOrMorePendingTasks() {
        long pendingCount = taskRepository.countByStatus(TaskStatus.PENDING);
        if (pendingCount >= 7) {
            NotificationDTO notification = new NotificationDTO(
                    "⚠️ 7 or more tasks are in pending status!",
                    (int) pendingCount
            );
            messagingTemplate.convertAndSend("/topic/notifications", notification);
        }
    }
}
