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

    @PostMapping
    public ResponseEntity<JobOffer> createJobOffer(@RequestBody JobOffer jobOffer) {
        JobOffer newJobOffer = jobOfferService.createJobOffer(jobOffer);
        return ResponseEntity.status(HttpStatus.CREATED).body(newJobOffer);
    }

    @GetMapping
    public ResponseEntity<List<JobOffer>> getAllJobOffers() {
        return ResponseEntity.ok(jobOfferService.getAllJobOffers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobOffer> getJobOfferById(@PathVariable Long id) {
        return jobOfferService.getJobOfferById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobOffer> updateJobOffer(@PathVariable Long id, @RequestBody JobOffer updatedJobOffer) {
        JobOffer jobOffer = jobOfferService.updateJobOffer(id, updatedJobOffer); // Utilisation du service pour mettre à jour
        return ResponseEntity.ok(jobOffer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJobOffer(@PathVariable Long id) {
        jobOfferService.deleteJobOffer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/published")
    public ResponseEntity<List<JobOffer>> getPublishedJobOffers() {
        List<JobOffer> publishedOffers = jobOfferService.getPublishedJobOffers();
        return ResponseEntity.ok(publishedOffers);
    }
}
