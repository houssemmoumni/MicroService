package com.megaminds.incident.service;

import com.megaminds.incident.entity.Project;
import com.megaminds.incident.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    // Updated method name to match either repository option
    public List<Project> getPublishedProjects() {
        return projectRepository.findByPublishedTrue(); // or findPublishedProjects() if using Option 2
    }

    public Optional<Project> getProjectById(Long id) {
        return projectRepository.findById(id);
    }

    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

    public Project publishProject(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        project.setPublished(true);
        return projectRepository.save(project);
    }

    public Project unpublishProject(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        project.setPublished(false);
        return projectRepository.save(project);
    }

    public Project updateProject(Long id, Project projectDetails) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        project.setName(projectDetails.getName());
        project.setLocation(projectDetails.getLocation());
        project.setDescription(projectDetails.getDescription());
        project.setPublished(projectDetails.isPublished());

        if (projectDetails.getImage() != null) {
            project.setImage(projectDetails.getImage());
        }

        return projectRepository.save(project);
    }
}