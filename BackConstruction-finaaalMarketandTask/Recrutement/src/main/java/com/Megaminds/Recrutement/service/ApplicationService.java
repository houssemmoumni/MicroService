package com.Megaminds.Recrutement.service;

import com.Megaminds.Recrutement.entity.*;
import com.Megaminds.Recrutement.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final CandidateRepository candidateRepository;
    private final JobOfferRepository jobOfferRepository;
    private final InterviewRepository interviewRepository;

    public ApplicationService(ApplicationRepository applicationRepository,
                              CandidateRepository candidateRepository,
                              JobOfferRepository jobOfferRepository,
                              InterviewRepository interviewRepository) {
        this.applicationRepository = applicationRepository;
        this.candidateRepository = candidateRepository;
        this.jobOfferRepository = jobOfferRepository;
        this.interviewRepository = interviewRepository;
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

    public Application getApplicationById(Long id) {
        return applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));
    }

    public Application updateApplicationStatus(Long id, ApplicationStatus status) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        application.setStatus(status);

        if (status == ApplicationStatus.ACCEPTED) {
            createInterviewForAcceptedApplication(application);
        }

        return applicationRepository.save(application);
    }

    private void createInterviewForAcceptedApplication(Application application) {
        Interview interview = new Interview();
        interview.setInterviewDate(LocalDate.now().plusDays(7));
        interview.setFeedback("");
        interview.setApplication(application);
        interviewRepository.save(interview);
    }

    public void deleteApplication(Long id) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));
        applicationRepository.delete(application);
    }
}