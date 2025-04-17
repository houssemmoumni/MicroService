package com.Megaminds.Recrutement.repository;

import com.Megaminds.Recrutement.entity.JobOffer;
import com.Megaminds.Recrutement.entity.JobOfferStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobOfferRepository extends JpaRepository<JobOffer, Long> {
    List<JobOffer> findByStatus(JobOfferStatus status); // Find by status (OPEN, CLOSED, etc.)

    List<JobOffer> findByPublishTrue(); // Find published job offers (published == true)
}
