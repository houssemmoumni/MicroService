package com.megaminds.task.service;

import com.megaminds.task.dto.TaskRequest;
import com.megaminds.task.dto.TaskResponse;
import com.megaminds.task.entity.Task;
import com.megaminds.task.entity.TaskStatus;
import com.megaminds.task.entity.User;
import com.megaminds.task.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskMapper {
    private static final Integer PROJECT_MANAGER_ID = 4; // Static Project Manager ID
    private final UserRepository userRepository; // Inject repository to fetch project manager


    public Task toTask(TaskRequest request, User assignedTo) {
        User projectManager = userRepository.findById(PROJECT_MANAGER_ID)
                .orElseThrow(() -> new RuntimeException("Project Manager not found"));
        return Task.builder()
                .title(request.title()) // Use title() instead of getTitle()
                .description(request.description()) // Use description() instead of getDescription()
                .status(TaskStatus.valueOf(request.status())) // Default status for new tasks
                .dueDate(request.dueDate()) // Use dueDate() instead of getDueDate()
                .assignedBy(projectManager) // Assign the task from the project manager
                .assignedTo(assignedTo)
                .build();
    }

    public TaskResponse toTaskResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus().name(),
                task.getDueDate(),
                "Project Manager", // Affichage statique
                task.getAssignedTo().getUsername()
        );
    }
}
