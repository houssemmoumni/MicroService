package com.megaminds.incident.controllers;

import com.megaminds.incident.repository.ProjectRepository;
import com.megaminds.incident.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/debug")
public class DebugController {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public DebugController(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/verify-entities")
    public ResponseEntity<Map<String, Object>> verifyEntities(
            @RequestParam Long projectId,
            @RequestParam(required = false) Long userId) {

        Map<String, Object> response = new HashMap<>();

        // Check project
        boolean projectExists = projectRepository.existsById(projectId);
        response.put("projectExists", projectExists);
        if (projectExists) {
            response.put("projectPublished", projectRepository.findById(projectId).get().isPublished());
        }

        // Check user if provided
        if (userId != null) {
            response.put("userExists", userRepository.existsById(userId));
        }

        return ResponseEntity.ok(response);
    }
}