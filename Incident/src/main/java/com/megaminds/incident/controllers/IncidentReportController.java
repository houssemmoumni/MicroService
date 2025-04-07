package com.megaminds.incident.controllers;

import com.megaminds.incident.dto.AssignIncidentRequest;
import com.megaminds.incident.dto.IncidentReportDTO;
import com.megaminds.incident.entity.*;
import com.megaminds.incident.repository.IncidentActionRepository;
import com.megaminds.incident.repository.IncidentReportRepository;
import com.megaminds.incident.repository.UserRepository;
import com.megaminds.incident.service.IncidentReportService;
import com.megaminds.incident.service.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/incidents")
@CrossOrigin(origins = "http://localhost:4200")
public class IncidentReportController {

    private final IncidentReportService incidentReportService;
    private final NotificationService notificationService;
    private final SimpMessagingTemplate messagingTemplate;
    private final UserRepository userRepository;
    private final IncidentReportRepository incidentReportRepository;
    private final IncidentActionRepository incidentActionRepository;

    public IncidentReportController(
            IncidentReportService incidentReportService,
            NotificationService notificationService,
            SimpMessagingTemplate messagingTemplate,
            UserRepository userRepository,
            IncidentReportRepository incidentReportRepository,
            IncidentActionRepository incidentActionRepository) {
        this.incidentReportService = incidentReportService;
        this.notificationService = notificationService;
        this.messagingTemplate = messagingTemplate;
        this.userRepository = userRepository;
        this.incidentReportRepository = incidentReportRepository;
        this.incidentActionRepository = incidentActionRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<IncidentReport> getIncidentById(@PathVariable Long id) {
        IncidentReport incident = incidentReportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Incident not found"));
        return ResponseEntity.ok(incident);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createIncident(@RequestBody IncidentReportDTO incidentReportDTO) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (incidentReportDTO.getProjectId() == null) {
                throw new IllegalArgumentException("Project ID is required");
            }
            if (incidentReportDTO.getDescription() == null || incidentReportDTO.getDescription().trim().isEmpty()) {
                throw new IllegalArgumentException("Description is required");
            }
            if (incidentReportDTO.getReporterName() == null || incidentReportDTO.getReporterName().trim().isEmpty()) {
                throw new IllegalArgumentException("Reporter name is required");
            }

            IncidentReport incidentReport = new IncidentReport();
            incidentReport.setDescription(incidentReportDTO.getDescription());
            incidentReport.setReportDate(LocalDate.now());
            incidentReport.setStatus(IncidentStatus.DECLARED);
            incidentReport.setSeverity(incidentReportDTO.getSeverity() != null ?
                    incidentReportDTO.getSeverity() : IncidentSeverity.MEDIUM);

            IncidentReport savedIncident = incidentReportService.createIncident(
                    incidentReport,
                    incidentReportDTO.getReportedById(),
                    incidentReportDTO.getProjectId()
            );

            String formattedTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
            Notification notification = new Notification();
            notification.setMessage(String.format(
                    "New %s severity incident at %s reported by %s: %s",
                    savedIncident.getSeverity(),
                    formattedTime,
                    incidentReportDTO.getReporterName(),
                    savedIncident.getDescription()
            ));
            notification.setNotificationDate(LocalDateTime.now());
            notification.setRead(false);
            notification.setSeverity(savedIncident.getSeverity().name());
            notification.setReceiverId(1L);
            notification.setIncidentReport(savedIncident);

            Notification savedNotification = notificationService.createNotification(notification);
            messagingTemplate.convertAndSend("/topic/notifications", savedNotification);

            response.put("data", savedIncident);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("error", "Failed to create incident: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("/{incidentId}/assign")
    public ResponseEntity<IncidentReport> assignIncident(
            @PathVariable Long incidentId,
            @RequestBody AssignIncidentRequest request) {

        IncidentReport updatedIncident = incidentReportService.assignIncident(
                incidentId,
                request.getTechnicianId(),
                request.getAdminId(),
                request.getComments()
        );

        // Create incident action
        IncidentAction action = new IncidentAction();
        action.setDescription(request.getComments() != null ? request.getComments() : "Incident assigned to technician");
        action.setActionDate(LocalDate.now());
        action.setActionType(IncidentActionType.ASSIGNED);
        action.setIncidentReport(updatedIncident);
        action.setPerformedBy(userRepository.findById(request.getAdminId()).orElse(null));
        incidentActionRepository.save(action);

        // Notification to technician
        User TECHNICIEN = userRepository.findById(request.getTechnicianId())
                .orElseThrow(() -> new RuntimeException("Technician not found"));

        Notification notification = new Notification();
        notification.setMessage("New incident assigned to you: " + updatedIncident.getDescription());
        notification.setNotificationDate(LocalDateTime.now());
        notification.setRead(false);
        notification.setSeverity(updatedIncident.getSeverity().name());
        notification.setReceiverId(request.getTechnicianId());
        notification.setIncidentReport(updatedIncident);

        Notification savedNotification = notificationService.createNotification(notification);
        messagingTemplate.convertAndSend("/topic/notifications", savedNotification);

        return ResponseEntity.ok(updatedIncident);
    }

    @GetMapping("/technicians")
    public ResponseEntity<List<User>> getAllTechnicians() {
        return ResponseEntity.ok(userRepository.findByRole(UserRole.TECHNICIEN));
    }

    @GetMapping
    public ResponseEntity<List<IncidentReport>> getAllIncidents() {
        return ResponseEntity.ok(incidentReportRepository.findAll());
    }
}