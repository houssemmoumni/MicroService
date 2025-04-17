package com.Megaminds.Recrutement.repository;

import com.Megaminds.Recrutement.entity.Interview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InterviewRepository extends JpaRepository<Interview, Long> {
    List<Interview> findByApplicationId(Long applicationId);
    Optional<Interview> findByToken(String token);
    List<Interview> findByCompletedTrue();
}