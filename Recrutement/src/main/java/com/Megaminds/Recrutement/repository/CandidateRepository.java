package com.Megaminds.Recrutement.repository;

import com.Megaminds.Recrutement.entity.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {
}
