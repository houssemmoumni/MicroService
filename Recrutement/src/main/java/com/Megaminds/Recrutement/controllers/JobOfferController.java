package com.Megaminds.Recrutement.controllers;

import com.Megaminds.Recrutement.entity.JobOffer;
import com.Megaminds.Recrutement.service.JobOfferService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/job-offers")
public class JobOfferController {

    private final JobOfferService jobOfferService;

    public JobOfferController(JobOfferService jobOfferService) {
        this.jobOfferService = jobOfferService;
    }

    // Crée une nouvelle offre d'emploi
    @PostMapping
    public ResponseEntity<JobOffer> createJobOffer(@RequestBody JobOffer jobOffer) {
        JobOffer newJobOffer = jobOfferService.createJobOffer(jobOffer);
        return ResponseEntity.status(HttpStatus.CREATED).body(newJobOffer);
    }

    // Récupère toutes les offres d'emploi
    @GetMapping
    public ResponseEntity<List<JobOffer>> getAllJobOffers() {
        return ResponseEntity.ok(jobOfferService.getAllJobOffers());
    }

    // Récupère une offre d'emploi par son ID
    @GetMapping("/{id}")
    public ResponseEntity<JobOffer> getJobOfferById(@PathVariable Long id) {
        return jobOfferService.getJobOfferById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Met à jour une offre d'emploi
    @PutMapping("/{id}")
    public ResponseEntity<JobOffer> updateJobOffer(@PathVariable Long id, @RequestBody JobOffer updatedJobOffer) {
        JobOffer jobOffer = jobOfferService.updateJobOffer(id, updatedJobOffer);
        return ResponseEntity.ok(jobOffer);
    }

    // Supprime une offre d'emploi
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJobOffer(@PathVariable Long id) {
        jobOfferService.deleteJobOffer(id);
        return ResponseEntity.noContent().build();
    }

    // Publie une offre d'emploi
    @PutMapping("/{id}/publish")
    public ResponseEntity<JobOffer> publishJobOffer(@PathVariable Long id) {
        try {
            JobOffer publishedJobOffer = jobOfferService.publishJobOffer(id);
            return ResponseEntity.ok(publishedJobOffer);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Récupère les offres publiées
    @GetMapping("/published")
    public ResponseEntity<List<JobOffer>> getPublishedJobOffers() {
        List<JobOffer> publishedOffers = jobOfferService.getPublishedJobOffers();
        return ResponseEntity.ok(publishedOffers);
    }
}