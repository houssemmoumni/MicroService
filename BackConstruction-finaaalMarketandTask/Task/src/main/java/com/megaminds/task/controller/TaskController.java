package com.megaminds.task.controller;

import com.megaminds.task.dto.TaskAssignmentRequest;
import com.megaminds.task.dto.TaskAssignmentResponse;
import com.megaminds.task.dto.TaskRequest;
import com.megaminds.task.dto.TaskResponse;
import com.megaminds.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    private static final int PROJECT_MANAGER_ID = 4; // Static Project Manager ID


    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@RequestBody TaskRequest request) {
        return ResponseEntity.ok(taskService.createTask(request, PROJECT_MANAGER_ID));
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Integer taskId) {
        return ResponseEntity.ok(taskService.getTaskById(taskId));
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TaskResponse>> getTaskByUserId(@PathVariable Integer userId) {
        return ResponseEntity.ok(taskService.getAllTasksByUser(userId));
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Integer taskId, @RequestBody TaskRequest request) {
        return ResponseEntity.ok(taskService.updateTask(taskId, request));
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Integer taskId) {
        taskService.deleteTask(taskId, PROJECT_MANAGER_ID);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/assign")
    public ResponseEntity<TaskAssignmentResponse> assignTask(@RequestBody TaskAssignmentRequest request) {
        return ResponseEntity.ok(taskService.assignTask(request));
    }

    @GetMapping("/worker")
    public ResponseEntity<List<TaskResponse>> getTasksForWorker() {
        return ResponseEntity.ok(taskService.getTasksByWorker());
    }
}
