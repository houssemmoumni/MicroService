package com.Megaminds.Recrutement.controllers;

import com.Megaminds.Recrutement.entity.*;
import com.Megaminds.Recrutement.repository.*;
import com.Megaminds.Recrutement.service.EmailService;
import com.Megaminds.Recrutement.service.InterviewService;
import com.Megaminds.Recrutement.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/applications")
@CrossOrigin(origins = "http://localhost:4200")
public class ApplicationController {

    private final ApplicationRepository applicationRepository;
    private final CandidateRepository candidateRepository;
    private final JobOfferRepository jobOfferRepository;
    private final EmailService emailService;
    private final NotificationService notificationService;
    private final InterviewRepository interviewRepository;
    private final InterviewService interviewService;

    public ApplicationController(ApplicationRepository applicationRepository,
                                 CandidateRepository candidateRepository,
                                 JobOfferRepository jobOfferRepository,
                                 EmailService emailService,
                                 NotificationService notificationService,
                                 InterviewRepository interviewRepository,
                                 InterviewService interviewService) {
        this.applicationRepository = applicationRepository;
        this.candidateRepository = candidateRepository;
        this.jobOfferRepository = jobOfferRepository;
        this.interviewService = interviewService;
        this.emailService = emailService;
        this.notificationService = notificationService;
        this.interviewRepository = interviewRepository;
    }

    @PostMapping(produces = "application/json")
    public ResponseEntity<?> createApplication(
            @RequestParam(value = "candidateId", required = false) Long candidateId,
            @RequestParam("jobOfferId") Long jobOfferId,
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("email") String email,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam("address") String address,
            @RequestParam("resumeFile") MultipartFile resumeFile) {

        Optional<JobOffer> jobOfferOpt = jobOfferRepository.findById(jobOfferId);

        if (jobOfferOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Offre d'emploi introuvable.");
        }

        try {
            Candidate candidate;
            if (candidateId != null) {
                candidate = candidateRepository.findById(candidateId)
                        .orElseThrow(() -> new RuntimeException("Candidat introuvable"));
            } else {
                candidate = new Candidate(); // constructeur vide
            }

            // Mettre à jour les informations du candidat
            candidate.setFirstName(firstName);
            candidate.setLastName(lastName);
            candidate.setEmail(email);
            candidate.setPhoneNumber(phoneNumber);
            candidate.setAddress(address);
            candidateRepository.save(candidate);

            Application application = new Application();
            application.setCandidate(candidate);
            application.setJobOffer(jobOfferOpt.get());
            application.setDate(LocalDate.now());
            application.setResume(resumeFile.getBytes());
            application.setStatus(ApplicationStatus.PENDING);

            applicationRepository.save(application);

            return ResponseEntity.ok(Collections.singletonMap("message", "Candidature soumise avec succès !"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erreur lors de l'enregistrement de la candidature.");
        }
    }

    @GetMapping
    public ResponseEntity<List<Application>> getAllApplications() {
        List<Application> applications = applicationRepository.findAll();
        return ResponseEntity.ok(applications);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateApplicationStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        String status = request.get("status");
        Optional<Application> applicationOpt = applicationRepository.findById(id);

        if (applicationOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Application not found");
        }

        try {
            Application application = applicationOpt.get();
            application.setStatus(ApplicationStatus.valueOf(status.toUpperCase()));
            applicationRepository.save(application);

            Candidate candidate = application.getCandidate();

            String subject = "Application Status Update";
            String text;
            if (status.equalsIgnoreCase("ACCEPTED")) {
                text = "Congratulations! Your application for " +
                        application.getJobOffer().getTitle() + " has been accepted.";

                // Schedule automated interview
                Interview interview = interviewService.scheduleAutomatedInterview(application);

                notificationService.createNotification(
                        "success",
                        "Interview scheduled for " + candidate.getFirstName() + " " + candidate.getLastName(),
                        application.getId(),
                        interview.getId()
                );
            } else if (status.equalsIgnoreCase("REJECTED")) {
                text = "We regret to inform you that your application for " +
                        application.getJobOffer().getTitle() + " has been rejected.";
            } else {
                text = "Your application status for " +
                        application.getJobOffer().getTitle() + " has been updated.";
            }

            emailService.sendEmail(candidate.getEmail(), subject, text);
            return ResponseEntity.ok(Collections.singletonMap("message", "Status updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error updating status: " + e.getMessage());
        }
    }
}
