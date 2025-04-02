package com.megaminds.incident.controllers;

import com.megaminds.incident.dto.IncidentReportDTO;
import com.megaminds.incident.entity.*;
import com.megaminds.incident.service.IncidentReportService;
import com.megaminds.incident.service.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/incidents")
@CrossOrigin(origins = "http://localhost:4200")
public class IncidentReportController {

    private final IncidentReportService incidentReportService;
    private final NotificationService notificationService;
    private final SimpMessagingTemplate messagingTemplate;

    public IncidentReportController(
            IncidentReportService incidentReportService,
            NotificationService notificationService,
            SimpMessagingTemplate messagingTemplate
    ) {
        this.incidentReportService = incidentReportService;
        this.notificationService = notificationService;
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createIncident(@RequestBody IncidentReportDTO incidentReportDTO) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Validation
            if (incidentReportDTO.getProjectId() == null) {
                response.put("error", "Project ID is required");
                response.put("status", HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(response);
            }

            if (incidentReportDTO.getDescription() == null || incidentReportDTO.getDescription().trim().isEmpty()) {
                response.put("error", "Description is required");
                response.put("status", HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(response);
            }

            // Create incident entity
            IncidentReport incidentReport = new IncidentReport();
            incidentReport.setDescription(incidentReportDTO.getDescription());
            incidentReport.setReportDate(incidentReportDTO.getReportDate());
            incidentReport.setStatus(incidentReportDTO.getStatus());
            incidentReport.setSeverity(incidentReportDTO.getSeverity());

            // Save incident
            IncidentReport saved = incidentReportService.createIncident(
                    incidentReport,
                    incidentReportDTO.getReportedById(),
                    incidentReportDTO.getProjectId()
            );

            // Create and send notification
            Notification notification = new Notification();
            notification.setMessage("New incident reported by " + incidentReportDTO.getReporterName() +
                    ": " + incidentReportDTO.getDescription());
            notification.setNotificationDate(LocalDateTime.now());
            notification.setRead(false);
            notification.setSeverity(incidentReportDTO.getSeverity().name());

            messagingTemplate.convertAndSend("/topic/notifications", notification);

            // Return response
            response.put("data", saved);
            response.put("status", HttpStatus.CREATED.value());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            response.put("error", "Failed to create incident: " + e.getMessage());
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}