package com.Megaminds.Recrutement.service;

import com.Megaminds.Recrutement.entity.Application;
import com.Megaminds.Recrutement.entity.ApplicationStatus;
import com.Megaminds.Recrutement.entity.Candidate;
import com.Megaminds.Recrutement.entity.JobOffer;
import com.Megaminds.Recrutement.repository.ApplicationRepository;
import com.Megaminds.Recrutement.repository.CandidateRepository;
import com.Megaminds.Recrutement.repository.JobOfferRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    public Application applyForJob(Long candidateId, Long jobOfferId, MultipartFile resumeFile) throws IOException {
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));

        JobOffer jobOffer = jobOfferRepository.findById(jobOfferId)
                .orElseThrow(() -> new RuntimeException("Job Offer not found"));

        Application application = new Application();
        application.setCandidate(candidate);
        application.setJobOffer(jobOffer);
        application.setStatus(ApplicationStatus.PENDING);

        if (resumeFile != null && !resumeFile.isEmpty()) {
            application.setResume(resumeFile.getBytes());
        }

        return applicationRepository.save(application);
    }

    public List<Application> getAllApplications() {
        return applicationRepository.findAll();
    }

    public Optional<Application> getApplicationById(Long id) {
        return applicationRepository.findById(id);
    }

    public Application updateApplicationStatus(Long id, String status) {
        return applicationRepository.findById(id).map(application -> {
            application.setStatus(ApplicationStatus.valueOf(status.toUpperCase()));
            return applicationRepository.save(application);
        }).orElseThrow(() -> new RuntimeException("Application not found"));
    }

    public void deleteApplication(Long id) {
        applicationRepository.deleteById(id);
    }
}
