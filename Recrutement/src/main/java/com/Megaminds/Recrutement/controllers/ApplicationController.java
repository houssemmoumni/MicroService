package com.Megaminds.Recrutement.controllers;

import com.Megaminds.Recrutement.entity.Application;
import com.Megaminds.Recrutement.entity.ApplicationStatus;
import com.Megaminds.Recrutement.entity.Candidate;
import com.Megaminds.Recrutement.entity.JobOffer;
import com.Megaminds.Recrutement.repository.ApplicationRepository;
import com.Megaminds.Recrutement.repository.CandidateRepository;
import com.Megaminds.Recrutement.repository.JobOfferRepository;
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

    public ApplicationController(ApplicationRepository applicationRepository, CandidateRepository candidateRepository, JobOfferRepository jobOfferRepository) {
        this.applicationRepository = applicationRepository;
        this.candidateRepository = candidateRepository;
        this.jobOfferRepository = jobOfferRepository;
    }

    // Endpoint for candidates to submit an application
    @PostMapping(produces = "application/json")
    public ResponseEntity<?> createApplication(
            @RequestParam("candidateId") Long candidateId,
            @RequestParam("jobOfferId") Long jobOfferId,
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("email") String email,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam("address") String address,
            @RequestParam("resumeFile") MultipartFile resumeFile) {

        Optional<Candidate> candidateOpt = candidateRepository.findById(candidateId);
        Optional<JobOffer> jobOfferOpt = jobOfferRepository.findById(jobOfferId);

        if (candidateOpt.isEmpty() || jobOfferOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Candidat ou offre d'emploi introuvable.");
        }

        try {
            // Update candidate information
            Candidate candidate = candidateOpt.get();
            candidate.setFirstName(firstName);
            candidate.setLastName(lastName);
            candidate.setEmail(email);
            candidate.setPhoneNumber(phoneNumber);
            candidate.setAddress(address);
            candidateRepository.save(candidate);

            // Create a new application
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

    // Endpoint for admin to fetch all applications
    @GetMapping
    public ResponseEntity<List<Application>> getAllApplications() {
        List<Application> applications = applicationRepository.findAll();
        return ResponseEntity.ok(applications);
    }

    // Endpoint to update the status of an application
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateApplicationStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        String status = request.get("status");
        Optional<Application> applicationOpt = applicationRepository.findById(id);

        if (applicationOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Candidature introuvable.");
        }

        try {
            Application application = applicationOpt.get();
            application.setStatus(ApplicationStatus.valueOf(status.toUpperCase()));
            applicationRepository.save(application);
            return ResponseEntity.ok(Collections.singletonMap("message", "Statut mis à jour avec succès !"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erreur lors de la mise à jour du statut.");
        }
    }
}