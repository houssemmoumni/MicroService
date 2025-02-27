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

    public JobOffer createJobOffer(JobOffer jobOffer) {
        jobOffer.setPostedDate(LocalDate.now());

        // Vérifie si un statut est déjà défini, sinon assigne OPEN par défaut
        if (jobOffer.getStatus() == null) {
            jobOffer.setStatus(JobOfferStatus.OPEN);
        }

        return jobOfferRepository.save(jobOffer);
    }


    public List<JobOffer> getAllJobOffers() {
        return jobOfferRepository.findAll();
    }

    public Optional<JobOffer> getJobOfferById(Long id) {
        return jobOfferRepository.findById(id);
    }

    public JobOffer updateJobOffer(Long id, JobOffer updatedJobOffer) {
        return jobOfferRepository.findById(id).map(jobOffer -> {
            jobOffer.setTitle(updatedJobOffer.getTitle());
            jobOffer.setDescription(updatedJobOffer.getDescription());
            jobOffer.setStatus(updatedJobOffer.getStatus());
            return jobOfferRepository.save(jobOffer);
        }).orElseThrow(() -> new RuntimeException("Job Offer not found"));
    }

    public void deleteJobOffer(Long id) {
        jobOfferRepository.deleteById(id);
    }
    public JobOffer publishJobOffer(Long id) {
        return jobOfferRepository.findById(id).map(jobOffer -> {
            // Met à jour les champs nécessaires
            jobOffer.setPublish(true);
            jobOffer.setStatus(JobOfferStatus.PUBLISHED);

            // Assure que le titre et la date de publication sont bien présents
            if (jobOffer.getTitle() == null || jobOffer.getTitle().isEmpty()) {
                jobOffer.setTitle("Titre par défaut"); // Remplacez par une valeur par défaut appropriée
            }
            if (jobOffer.getPostedDate() == null) {
                jobOffer.setPostedDate(LocalDate.now()); // Définit la date de publication actuelle
            }

            return jobOfferRepository.save(jobOffer);
        }).orElseThrow(() -> new RuntimeException("Job Offer not found"));
    }





    public List<JobOffer> getPublishedJobOffers() {
        return jobOfferRepository.findByStatus(JobOfferStatus.PUBLISHED);
    }


}

