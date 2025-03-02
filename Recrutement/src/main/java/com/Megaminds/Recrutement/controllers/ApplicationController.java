package com.Megaminds.Recrutement.controllers;

import com.Megaminds.Recrutement.dto.ApplicationDTO;
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
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/applications")
@CrossOrigin(origins = "http://localhost:4200")
public class ApplicationController {

    private static final Logger LOGGER = Logger.getLogger(ApplicationController.class.getName());

    private final ApplicationRepository applicationRepository;
    private final CandidateRepository candidateRepository;
    private final JobOfferRepository jobOfferRepository;

    public ApplicationController(ApplicationRepository applicationRepository, CandidateRepository candidateRepository, JobOfferRepository jobOfferRepository) {
        this.applicationRepository = applicationRepository;
        this.candidateRepository = candidateRepository;
        this.jobOfferRepository = jobOfferRepository;
    }

    // ✅ Endpoint pour soumettre une candidature
    @PostMapping(produces = "application/json")
    public ResponseEntity<?> createApplication(
            @RequestParam("candidateId") Long candidateId,
            @RequestParam("jobOfferId") Long jobOfferId,
            @RequestParam("resumeFile") MultipartFile resumeFile) {

        Optional<Candidate> candidateOpt = candidateRepository.findById(candidateId);
        Optional<JobOffer> jobOfferOpt = jobOfferRepository.findById(jobOfferId);

        if (candidateOpt.isEmpty() || jobOfferOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Candidat ou offre d'emploi introuvable.");
        }

        try {
            Application application = new Application();
            application.setCandidate(candidateOpt.get());
            application.setJobOffer(jobOfferOpt.get());
            application.setDate(LocalDate.now());
            application.setResume(resumeFile.getBytes());
            application.setStatus(ApplicationStatus.PENDING); // Définir le statut initial

            applicationRepository.save(application);

            return ResponseEntity.ok(Collections.singletonMap("message", "Candidature soumise avec succès !"));

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de l'enregistrement de la candidature", e);
            return ResponseEntity.internalServerError().body("Erreur lors de l'enregistrement de la candidature.");
        }
    }

    // ✅ Endpoint pour récupérer les candidatures d'un candidat
    @GetMapping("/candidate/{candidateId}")
    public ResponseEntity<List<ApplicationDTO>> getApplicationsByCandidate(@PathVariable Long candidateId) {
        List<Application> applications = applicationRepository.findByCandidateId(candidateId);
        if (applications.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<ApplicationDTO> applicationDTOs = applications.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(applicationDTOs);
    }

    // ✅ Endpoint pour récupérer toutes les candidatures
    @GetMapping
    public ResponseEntity<List<ApplicationDTO>> getAllApplications() {
        List<Application> applications = applicationRepository.findAll();
        List<ApplicationDTO> applicationDTOs = applications.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(applicationDTOs);
    }

    // ✅ Endpoint pour mettre à jour le statut d'une candidature
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateApplicationStatus(@PathVariable Long id, @RequestParam ApplicationStatus status) {
        Optional<Application> applicationOpt = applicationRepository.findById(id);
        if (applicationOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Application application = applicationOpt.get();
        application.setStatus(status);
        applicationRepository.save(application);

        return ResponseEntity.ok().build();
    }

    // Helper method to convert Application to ApplicationDTO
    private ApplicationDTO convertToDTO(Application application) {
        ApplicationDTO dto = new ApplicationDTO();
        dto.setId(application.getId());

        // Compute fullName dynamically
        String fullName = application.getCandidate().getFirstName() + " " + application.getCandidate().getLastName();
        dto.setCandidateFullName(fullName);

        dto.setCandidateEmail(application.getCandidate().getEmail());
        dto.setJobOfferTitle(application.getJobOffer().getTitle());
        dto.setStatus(application.getStatus().toString());

        // Encode resume to Base64
        if (application.getResume() != null) {
            dto.setResume(Base64.getEncoder().encodeToString(application.getResume()));
        } else {
            dto.setResume(null);
        }

        return dto;
    }
}