package com.megaminds.incident.service;

import com.megaminds.incident.entity.Project;
import com.megaminds.incident.entity.IncidentReport;
import com.megaminds.incident.repository.ProjectRepository;
import com.megaminds.incident.repository.IncidentReportRepository;
import com.megaminds.incident.repository.NotificationRepository;  // Added for notifications
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final IncidentReportRepository incidentReportRepository;
    private final NotificationRepository notificationRepository;  // Added for notifications

    // Constructor injection for both repositories
    public ProjectService(ProjectRepository projectRepository, IncidentReportRepository incidentReportRepository, NotificationRepository notificationRepository) {
        this.projectRepository = projectRepository;
        this.incidentReportRepository = incidentReportRepository;  // Inject the repository
        this.notificationRepository = notificationRepository;  // Inject the notification repository
    }

    // Method to create a new project
    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    // Method to retrieve all projects
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    // Method to get published projects
    public List<Project> getPublishedProjects() {
        return projectRepository.findByPublishedTrue();
    }

    // Method to delete all incident reports associated with a project
    public void deleteIncidentReports(Long projectId) {
        // Fetch all incident reports for the given project
        List<IncidentReport> incidentReports = incidentReportRepository.findByProjectId(projectId);

        // Iterate through all incident reports and delete related notifications first
        for (IncidentReport incidentReport : incidentReports) {
            // Delete notifications related to the incident report
            notificationRepository.deleteByIncidentReportId(incidentReport.getId());

            // Delete the incident report itself
            incidentReportRepository.delete(incidentReport);
        }
    }

    // Method to fetch a project by its ID
    public Optional<Project> getProjectById(Long id) {
        return projectRepository.findById(id);
    }

    // Method to delete a project along with its incident reports
    public void deleteProject(Long projectId) {
        deleteIncidentReports(projectId);  // First delete related incident reports
        projectRepository.deleteById(projectId);  // Then delete the project
    }

    // Method to publish a project
    public Project publishProject(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        project.setPublished(true);
        return projectRepository.save(project);
    }

    // Method to unpublish a project
    public Project unpublishProject(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        project.setPublished(false);
        return projectRepository.save(project);
    }

    // Method to update project details
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
