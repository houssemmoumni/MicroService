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
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import jakarta.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
    private final JavaMailSender mailSender;

    public IncidentReportController(
            IncidentReportService incidentReportService,
            NotificationService notificationService,
            SimpMessagingTemplate messagingTemplate,
            UserRepository userRepository,
            IncidentReportRepository incidentReportRepository,
            IncidentActionRepository incidentActionRepository,
            JavaMailSender mailSender) {
        this.incidentReportService = incidentReportService;
        this.notificationService = notificationService;
        this.messagingTemplate = messagingTemplate;
        this.userRepository = userRepository;
        this.incidentReportRepository = incidentReportRepository;
        this.incidentActionRepository = incidentActionRepository;
        this.mailSender = mailSender;
    }

    private static class EmailResult {
        final boolean success;
        final String errorMessage;

        EmailResult(boolean success, String errorMessage) {
            this.success = success;
            this.errorMessage = errorMessage;
        }
    }

    // Nouvelle méthode pour générer le lien de résolution
    @GetMapping("/{id}/resolve-link")
    public ResponseEntity<Map<String, String>> generateResolveLink(@PathVariable Long id) {
        IncidentReport incident = incidentReportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Incident not found"));

        if (incident.getResolutionToken() == null) {
            incident.setResolutionToken(UUID.randomUUID().toString());
            incidentReportRepository.save(incident);
        }

        Map<String, String> response = new HashMap<>();
        response.put("link", "http://localhost:4200/resolve/" + id + "?token=" + incident.getResolutionToken());
        return ResponseEntity.ok(response);
    }

    // Nouvelle méthode pour récupérer un incident par son token
    @GetMapping("/by-token/{token}")
    public ResponseEntity<IncidentReport> getIncidentByToken(@PathVariable String token) {
        IncidentReport incident = incidentReportRepository.findByResolutionToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid or expired token"));
        return ResponseEntity.ok(incident);
    }

    // Nouvelle méthode pour résoudre avec token
    @PatchMapping("/{id}/resolve-with-token")
    public ResponseEntity<IncidentReport> resolveWithToken(
            @PathVariable Long id,
            @RequestParam boolean resolved,
            @RequestParam String token) {

        IncidentReport incident = incidentReportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Incident not found"));

        if (!incident.getResolutionToken().equals(token)) {
            throw new RuntimeException("Invalid token");
        }

        incident.setStatus(resolved ? IncidentStatus.RESOLVED : IncidentStatus.REOPENED);
        incident.setResolutionToken(null);
        IncidentReport updatedIncident = incidentReportRepository.save(incident);

        IncidentAction action = new IncidentAction();
        action.setDescription(resolved ? "Incident resolved via token" : "Incident reopened via token");
        action.setActionDate(LocalDate.now());
        action.setActionType(resolved ? IncidentActionType.RESOLVED : IncidentActionType.REOPENED);
        action.setIncidentReport(updatedIncident);
        action.setPerformedBy(incident.getAssignedTo());
        incidentActionRepository.save(action);

        return ResponseEntity.ok(updatedIncident);
    }

    @PostMapping("/{incidentId}/assign")
    public ResponseEntity<?> assignIncident(
            @PathVariable Long incidentId,
            @RequestBody AssignIncidentRequest request) {

        Map<String, Object> response = new HashMap<>();

        try {
            IncidentReport updatedIncident = incidentReportService.assignIncident(
                    incidentId,
                    request.getTechnicianId(),
                    request.getAdminId(),
                    request.getComments()
            );

            // Génération du token lors de l'assignation
            updatedIncident.setResolutionToken(UUID.randomUUID().toString());
            incidentReportRepository.save(updatedIncident);

            IncidentAction action = new IncidentAction();
            action.setDescription(request.getComments() != null ?
                    request.getComments() : "Incident assigned to technician");
            action.setActionDate(LocalDate.now());
            action.setActionType(IncidentActionType.ASSIGNED);
            action.setIncidentReport(updatedIncident);
            action.setPerformedBy(userRepository.findById(request.getAdminId()).orElse(null));
            incidentActionRepository.save(action);

            User technician = userRepository.findById(request.getTechnicianId())
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

            // Mise à jour de l'email avec le lien de résolution
            EmailResult emailResult = sendAssignmentEmail(technician, updatedIncident);

            response.put("success", true);
            response.put("incident", updatedIncident);
            response.put("emailSent", emailResult.success);

            if (!emailResult.success) {
                response.put("emailError", emailResult.errorMessage);
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("error", "Failed to assign incident: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    private EmailResult sendAssignmentEmail(User technician, IncidentReport incident) {
        try {
            String resolveLink = "http://localhost:4200/resolve/" + incident.getId() + "?token=" + incident.getResolutionToken();

            SimpleMailMessage email = new SimpleMailMessage();
            email.setTo(technician.getEmail());
            email.setSubject("New Incident Assigned: #" + incident.getId());
            email.setText(
                    "Dear " + technician.getUsername() + ",\n\n" +
                            "You have been assigned a new incident:\n\n" +
                            "ID: " + incident.getId() + "\n" +
                            "Description: " + incident.getDescription() + "\n" +
                            "Severity: " + incident.getSeverity() + "\n" +
                            "Project: " + incident.getProject().getName() + "\n\n" +
                            "Please click the following link to resolve it:\n" +
                            resolveLink + "\n\n" +
                            "Thank you,\n" +
                            "Incident Management System"
            );

            mailSender.send(email);
            return new EmailResult(true, null);

        } catch (MailAuthenticationException e) {
            return new EmailResult(false, "SMTP Authentication Failed: " + e.getMessage());
        } catch (MailSendException e) {
            return new EmailResult(false, "Failed to send email: " + e.getMessage());
        } catch (Exception e) {
            return new EmailResult(false, "Unexpected error: " + e.getMessage());
        }
    }

    @PostConstruct
    public void debugMailConfig() {
        if (mailSender instanceof JavaMailSenderImpl) {
            JavaMailSenderImpl mailSenderImpl = (JavaMailSenderImpl) mailSender;
            System.out.println("===== SMTP CONFIGURATION =====");
            System.out.println("Host: " + mailSenderImpl.getHost());
            System.out.println("Port: " + mailSenderImpl.getPort());
            System.out.println("Username: " + mailSenderImpl.getUsername());
            System.out.println("Password: " +
                    (mailSenderImpl.getPassword() != null ? "******" : "NULL"));
            System.out.println("Protocol: " + mailSenderImpl.getProtocol());
            System.out.println("JavaMail Properties: " + mailSenderImpl.getJavaMailProperties());
        }
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

    @GetMapping("/test-email")
    public String testEmail() {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo("houssem.moumni@esprit.tn");
            message.setSubject("TEST from Incident Microservice");
            message.setText("If you get this, email is working!");
            mailSender.send(message);
            return "Email sent! Check inbox/spam.";
        } catch (Exception e) {
            return "Email failed: " + e.getMessage();
        }
    }

    @PatchMapping("/{id}/resolve")
    public ResponseEntity<IncidentReport> resolveIncident(
            @PathVariable Long id,
            @RequestParam boolean resolved,
            @RequestParam Long technicianId) {

        IncidentReport incident = incidentReportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Incident not found"));

        incident.setStatus(resolved ? IncidentStatus.RESOLVED : IncidentStatus.REOPENED);
        IncidentReport updatedIncident = incidentReportRepository.save(incident);

        IncidentAction action = new IncidentAction();
        action.setDescription(resolved ? "Incident resolved" : "Incident reopened");
        action.setActionDate(LocalDate.now());
        action.setActionType(resolved ? IncidentActionType.RESOLVED : IncidentActionType.REOPENED);
        action.setIncidentReport(updatedIncident);
        action.setPerformedBy(userRepository.findById(technicianId).orElse(null));
        incidentActionRepository.save(action);

        return ResponseEntity.ok(updatedIncident);
    }

    @GetMapping("/resolved")
    public ResponseEntity<List<IncidentReport>> getResolvedIncidents() {
        return ResponseEntity.ok(incidentReportRepository.findByStatus(IncidentStatus.RESOLVED));
    }

    @GetMapping("/unresolved")
    public ResponseEntity<List<IncidentReport>> getUnresolvedIncidents() {
        return ResponseEntity.ok(incidentReportRepository.findByStatusNot(IncidentStatus.RESOLVED));
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