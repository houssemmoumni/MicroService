package com.Megaminds.Recrutement.service;

import com.Megaminds.Recrutement.entity.Application;
import com.Megaminds.Recrutement.entity.ApplicationStatus;
import com.Megaminds.Recrutement.entity.Candidate;
import com.Megaminds.Recrutement.entity.JobOffer;
import com.Megaminds.Recrutement.repository.ApplicationRepository;
import com.Megaminds.Recrutement.repository.CandidateRepository;
import com.Megaminds.Recrutement.repository.JobOfferRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final CandidateRepository candidateRepository;
    private final JobOfferRepository jobOfferRepository;

    public ApplicationService(ApplicationRepository applicationRepository,
                              CandidateRepository candidateRepository,
                              JobOfferRepository jobOfferRepository) {
        this.applicationRepository = applicationRepository;
        this.candidateRepository = candidateRepository;
        this.jobOfferRepository = jobOfferRepository;
    }

    // Candidate applies for a job
    public Application applyForJob(Application application) {
        // Vérification de l'existence du candidat
        Candidate candidate = candidateRepository.findById(application.getCandidate().getId())
                .orElseThrow(() -> new RuntimeException("Candidate not found"));

        // Vérification de l'existence de l'offre d'emploi
        JobOffer jobOffer = jobOfferRepository.findById(application.getJobOffer().getId())
                .orElseThrow(() -> new RuntimeException("Job Offer not found"));

        application.setCandidate(candidate);
        application.setJobOffer(jobOffer);
        application.setStatus(ApplicationStatus.PENDING);
        return applicationRepository.save(application);
    }

    // Get all applications
    public List<Application> getAllApplications() {
        return applicationRepository.findAll();
    }

    // Get application by ID
    public Optional<Application> getApplicationById(Long id) {
        return applicationRepository.findById(id);
    }

    // Update application status
    public Application updateApplicationStatus(Long id, String status) {
        return applicationRepository.findById(id).map(application -> {
            application.setStatus(ApplicationStatus.valueOf(status.toUpperCase()));
            return applicationRepository.save(application);
        }).orElseThrow(() -> new RuntimeException("Application not found"));
    }

    // Delete an application
    public void deleteApplication(Long id) {
        applicationRepository.deleteById(id);
    }
}
