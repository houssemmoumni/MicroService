package com.Megaminds.Recrutement.service;

import com.Megaminds.Recrutement.entity.JobOffer;
import com.Megaminds.Recrutement.entity.JobOfferStatus;
import com.Megaminds.Recrutement.repository.JobOfferRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class JobOfferService {

    private final JobOfferRepository jobOfferRepository;

    public JobOfferService(JobOfferRepository jobOfferRepository) {
        this.jobOfferRepository = jobOfferRepository;
    }

    // Crée une nouvelle offre d'emploi
    public JobOffer createJobOffer(JobOffer jobOffer) {
        jobOffer.setPostedDate(LocalDate.now()); // Définit la date de publication
        if (jobOffer.getStatus() == null) {
            jobOffer.setStatus(JobOfferStatus.OPEN); // Statut par défaut : OPEN
        }
        return jobOfferRepository.save(jobOffer);
    }

    // Récupère toutes les offres d'emploi
    public List<JobOffer> getAllJobOffers() {
        return jobOfferRepository.findAll();
    }

    // Récupère une offre d'emploi par son ID
    public Optional<JobOffer> getJobOfferById(Long id) {
        return jobOfferRepository.findById(id);
    }

    // Met à jour une offre d'emploi
    public JobOffer updateJobOffer(Long id, JobOffer updatedJobOffer) {
        return jobOfferRepository.findById(id).map(jobOffer -> {
            jobOffer.setTitle(updatedJobOffer.getTitle());
            jobOffer.setDescription(updatedJobOffer.getDescription());
            jobOffer.setStatus(updatedJobOffer.getStatus());
            return jobOfferRepository.save(jobOffer);
        }).orElseThrow(() -> new RuntimeException("Offre d'emploi non trouvée"));
    }

    // Supprime une offre d'emploi
    public void deleteJobOffer(Long id) {
        jobOfferRepository.deleteById(id);
    }

    // Publie une offre d'emploi
    public JobOffer publishJobOffer(Long id) {
        return jobOfferRepository.findById(id).map(jobOffer -> {
            jobOffer.setPublish(true); // Publie l'offre
            jobOffer.setStatus(JobOfferStatus.PUBLISHED); // Met à jour le statut
            if (jobOffer.getPostedDate() == null) {
                jobOffer.setPostedDate(LocalDate.now()); // Définit la date de publication si elle n'existe pas
            }
            return jobOfferRepository.save(jobOffer);
        }).orElseThrow(() -> new RuntimeException("Offre d'emploi non trouvée"));
    }

    // Récupère les offres publiées
    public List<JobOffer> getPublishedJobOffers() {
        return jobOfferRepository.findByPublishTrue();
    }
}