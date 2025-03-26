package com.megaminds.incident.service;

import com.megaminds.incident.entity.*;
import com.megaminds.incident.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class IncidentReportService {
    private final IncidentReportRepository incidentReportRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final NotificationService notificationService;

    public IncidentReportService(IncidentReportRepository incidentReportRepository,
                                 UserRepository userRepository,
                                 ProjectRepository projectRepository,
                                 NotificationService notificationService) {
        this.incidentReportRepository = incidentReportRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.notificationService = notificationService;
    }

    @Transactional
    public IncidentReport createIncident(IncidentReport incidentReport) {
        // Validation des entités associées
        if (incidentReport.getReportedBy() != null) {
            User reporter = userRepository.findById(incidentReport.getReportedBy().getId())
                    .orElseThrow(() -> new RuntimeException("Utilisateur déclarant non trouvé"));
            incidentReport.setReportedBy(reporter);
        }

        if (incidentReport.getProject() != null) {
            Project project = projectRepository.findById(incidentReport.getProject().getId())
                    .orElseThrow(() -> new RuntimeException("Projet non trouvé"));
            incidentReport.setProject(project);
        }

        // Par défaut, le statut est DECLARED
        incidentReport.setStatus(IncidentStatus.DECLARED);

        IncidentReport savedIncident = incidentReportRepository.save(incidentReport);

        // Créer une notification pour les admins
        createAdminNotification(savedIncident);

        return savedIncident;
    }

    @Transactional
    public IncidentReport assignIncident(Long incidentId, Long technicianId) {
        IncidentReport incident = incidentReportRepository.findById(incidentId)
                .orElseThrow(() -> new RuntimeException("Incident non trouvé"));

        User technician = userRepository.findById(technicianId)
                .orElseThrow(() -> new RuntimeException("Technicien non trouvé"));

        incident.setAssignedTo(technician);
        incident.setStatus(IncidentStatus.ASSIGNED);

        IncidentReport updatedIncident = incidentReportRepository.save(incident);

        // Créer une notification pour le technicien
        createTechnicianNotification(updatedIncident);

        return updatedIncident;
    }

    public List<IncidentReport> getAllIncidents() {
        return incidentReportRepository.findAll();
    }

    public Optional<IncidentReport> getIncidentById(Long id) {
        return incidentReportRepository.findById(id);
    }

    public List<IncidentReport> getIncidentsByProject(Long projectId) {
        return incidentReportRepository.findByProjectId(projectId);
    }

    public List<IncidentReport> getIncidentsByStatus(IncidentStatus status) {
        return incidentReportRepository.findByStatus(status);
    }

    private void createAdminNotification(IncidentReport incident) {
        Notification notification = new Notification();
        notification.setMessage("Nouvel incident déclaré pour le projet: " +
                incident.getProject().getName());
        notification.setNotificationDate(java.time.LocalDateTime.now());
        notification.setRead(false);
        // Ici vous devriez définir l'admin comme receiver
        // notification.setReceiver(adminUser);
        notificationService.createNotification(notification);
    }

    private void createTechnicianNotification(IncidentReport incident) {
        if (incident.getAssignedTo() != null) {
            Notification notification = new Notification();
            notification.setMessage("Vous avez été assigné à l'incident: " +
                    incident.getDescription());
            notification.setNotificationDate(java.time.LocalDateTime.now());
            notification.setRead(false);
            notification.setReceiver(incident.getAssignedTo());
            notificationService.createNotification(notification);
        }
    }
}