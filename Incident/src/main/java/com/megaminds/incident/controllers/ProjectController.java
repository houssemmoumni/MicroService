// src/main/java/com/megaminds/incident/controllers/ProjectController.java
package com.megaminds.incident.controllers;

import com.megaminds.incident.entity.Project;
import com.megaminds.incident.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = "http://localhost:4200")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ResponseEntity<Project> createProject(@RequestBody Project project) {
        return ResponseEntity.ok(projectService.createProject(project));
    }

    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @GetMapping("/published")
    public ResponseEntity<List<Project>> getPublishedProjects() {
        return ResponseEntity.ok(projectService.getPublishedProjects());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long id) {
        Optional<Project> project = projectService.getProjectById(id);
        return project.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable Long id, @RequestBody Project projectDetails) {
        Project project = projectService.getProjectById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        project.setName(projectDetails.getName());
        project.setLocation(projectDetails.getLocation());
        project.setDescription(projectDetails.getDescription());
        return ResponseEntity.ok(projectService.createProject(project));
    }

    @PatchMapping("/{id}/publish")
    public ResponseEntity<Project> publishProject(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.publishProject(id));
    }

    @PatchMapping("/{id}/unpublish")
    public ResponseEntity<Project> unpublishProject(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.unpublishProject(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}