package com.megaminds.incident.controllers;

import com.megaminds.incident.entity.Project;
import com.megaminds.incident.service.ProjectService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = "http://localhost:4200")
public class ProjectController {
    private final ProjectService projectService;
    private final ObjectMapper objectMapper;

    public ProjectController(ProjectService projectService, ObjectMapper objectMapper) {
        this.projectService = projectService;
        this.objectMapper = objectMapper;
    }

    @PostMapping
    public ResponseEntity<Project> createProject(
            @RequestParam("project") String projectJson,
            @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {

        Project project = objectMapper.readValue(projectJson, Project.class);

        if (image != null && !image.isEmpty()) {
            project.setImage(Base64.getEncoder().encodeToString(image.getBytes()));
        }

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
        return projectService.getProjectById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(
            @PathVariable Long id,
            @RequestParam("project") String projectJson,
            @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {

        Project projectDetails = objectMapper.readValue(projectJson, Project.class);

        if (image != null && !image.isEmpty()) {
            projectDetails.setImage(Base64.getEncoder().encodeToString(image.getBytes()));
        }

        return ResponseEntity.ok(projectService.updateProject(id, projectDetails));
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