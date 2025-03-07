package com.Megaminds.Recrutement.service;

import com.Megaminds.Recrutement.entity.Application;
import com.Megaminds.Recrutement.entity.ApplicationStatus;
import com.Megaminds.Recrutement.entity.Candidate;
import com.Megaminds.Recrutement.entity.Interview;
import com.Megaminds.Recrutement.entity.JobOffer;
import com.Megaminds.Recrutement.repository.ApplicationRepository;
import com.Megaminds.Recrutement.repository.CandidateRepository;
import com.Megaminds.Recrutement.repository.InterviewRepository;
import com.Megaminds.Recrutement.repository.JobOfferRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ApplicationService implements IApplicationService{

    private final ApplicationRepository applicationRepository;
    private final CandidateRepository candidateRepository;
    private final JobOfferRepository jobOfferRepository;
    private final InterviewRepository interviewRepository; // Ajout du repository Interview

    public ApplicationService(ApplicationRepository applicationRepository,
                              CandidateRepository candidateRepository,
                              JobOfferRepository jobOfferRepository,
                              InterviewRepository interviewRepository) { // Injection du repository Interview
        this.applicationRepository = applicationRepository;
        this.candidateRepository = candidateRepository;
        this.jobOfferRepository = jobOfferRepository;
        this.interviewRepository = interviewRepository;
    }

    // Méthode pour postuler à une offre d'emploi
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

    // Méthode pour récupérer toutes les candidatures
    public List<Application> getAllApplications() {
        return applicationRepository.findAll();
    }

    // Méthode pour mettre à jour le statut d'une candidature
    public Application updateApplicationStatus(Long id, String status) {
        return applicationRepository.findById(id).map(application -> {
            application.setStatus(ApplicationStatus.valueOf(status.toUpperCase()));

            // Si la candidature est acceptée, générer un entretien automatiquement
            if (status.equalsIgnoreCase("ACCEPTED")) {
                Interview interview = new Interview();
                interview.setInterviewDate(LocalDate.now().plusDays(7)); // Entretien dans 7 jours
                interview.setFeedback(""); // Feedback vide par défaut
                interview.setApplication(application);
                interviewRepository.save(interview); // Enregistrer l'entretien
            }

            return applicationRepository.save(application);
        }).orElseThrow(() -> new RuntimeException("Application not found"));
    }
}