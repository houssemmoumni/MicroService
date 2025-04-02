package com.megaminds.incident.service;

import com.megaminds.incident.entity.*;
import com.megaminds.incident.repository.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;

@Service
public class IncidentReportService {
    private final IncidentReportRepository incidentReportRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public IncidentReportService(IncidentReportRepository incidentReportRepository,
                                 UserRepository userRepository,
                                 ProjectRepository projectRepository) {
        this.incidentReportRepository = incidentReportRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
    }

    @Transactional
    public IncidentReport createIncident(IncidentReport incidentReport, Long reportedById, Long projectId) {
        // 1. Get and verify project exists
        Project project = entityManager.find(Project.class, projectId);
        if (project == null || !project.isPublished()) {
            throw new IllegalArgumentException("Published project with ID " + projectId + " not found");
        }

        // 2. Get and verify user exists if provided
        User reporter = null;
        if (reportedById != null) {
            reporter = entityManager.find(User.class, reportedById);
            if (reporter == null) {
                throw new IllegalArgumentException("User with ID " + reportedById + " not found");
            }
        }

        // 3. Set the managed entities
        incidentReport.setProject(project);
        if (reporter != null) {
            incidentReport.setReportedBy(reporter);
        }

        // 4. Set defaults
        if (incidentReport.getStatus() == null) {
            incidentReport.setStatus(IncidentStatus.DECLARED);
        }
        if (incidentReport.getSeverity() == null) {
            incidentReport.setSeverity(IncidentSeverity.MEDIUM);
        }
        if (incidentReport.getReportDate() == null) {
            incidentReport.setReportDate(LocalDate.now());
        }

        // 5. Save using entity manager
        entityManager.persist(incidentReport);
        entityManager.flush();
        return incidentReport;
    }
}