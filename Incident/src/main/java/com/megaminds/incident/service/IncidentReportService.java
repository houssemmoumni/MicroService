package com.megaminds.incident.service;

import com.megaminds.incident.entity.*;
import com.megaminds.incident.repository.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class IncidentReportService {
    private final IncidentReportRepository incidentReportRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final IncidentActionRepository incidentActionRepository;
    private final NotificationRepository notificationRepository;
    private final WebSocketService webSocketService;

    @PersistenceContext
    private EntityManager entityManager;

    public IncidentReportService(IncidentReportRepository incidentReportRepository,
                                 UserRepository userRepository,
                                 ProjectRepository projectRepository,
                                 IncidentActionRepository incidentActionRepository,
                                 NotificationRepository notificationRepository,
                                 WebSocketService webSocketService) {
        this.incidentReportRepository = incidentReportRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.incidentActionRepository = incidentActionRepository;
        this.notificationRepository = notificationRepository;
        this.webSocketService = webSocketService;
    }

    @Transactional
    public IncidentReport createIncident(IncidentReport incidentReport, Long reportedById, Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));
        if (!project.isPublished()) {
            throw new IllegalArgumentException("Project is not published");
        }
        incidentReport.setProject(project);

        if (reportedById != null) {
            User reporter = userRepository.findById(reportedById)
                    .orElseThrow(() -> new IllegalArgumentException("Reporter not found"));
            incidentReport.setReportedBy(reporter);
        }

        if (incidentReport.getReportDate() == null) {
            incidentReport.setReportDate(LocalDate.now());
        }
        if (incidentReport.getStatus() == null) {
            incidentReport.setStatus(IncidentStatus.DECLARED);
        }
        if (incidentReport.getSeverity() == null) {
            incidentReport.setSeverity(IncidentSeverity.MEDIUM);
        }

        return incidentReportRepository.save(incidentReport);
    }

    @Transactional
    public IncidentReport assignIncident(Long incidentId, Long technicianId, Long adminId, String comments) {
        IncidentReport incident = incidentReportRepository.findById(incidentId)
                .orElseThrow(() -> new RuntimeException("Incident not found"));

        User technician = userRepository.findById(technicianId)
                .orElseThrow(() -> new RuntimeException("Technician not found"));

        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        incident.setAssignedTo(technician);
        incident.setStatus(IncidentStatus.ASSIGNED);
        IncidentReport savedIncident = incidentReportRepository.save(incident);

        IncidentAction assignmentAction = new IncidentAction();
        assignmentAction.setDescription(comments != null ? comments : "Incident assigned to technician");
        assignmentAction.setActionDate(LocalDate.now());
        assignmentAction.setActionType(IncidentActionType.ASSIGNED);
        assignmentAction.setIncidentReport(savedIncident);
        assignmentAction.setPerformedBy(admin);
        incidentActionRepository.save(assignmentAction);

        Notification notification = new Notification();
        notification.setReceiverId(technician.getId());
        notification.setMessage("New incident assigned: " + savedIncident.getDescription());
        notification.setIncidentReport(savedIncident);
        notification.setSeverity(savedIncident.getSeverity().toString());
        notification.setNotificationDate(LocalDateTime.now());
        notification.setRead(false);
        Notification savedNotification = notificationRepository.save(notification);

        webSocketService.sendIncidentNotification(savedNotification);

        return savedIncident;
    }
}