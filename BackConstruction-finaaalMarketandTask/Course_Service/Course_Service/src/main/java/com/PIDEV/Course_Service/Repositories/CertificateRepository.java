package com.PIDEV.Course_Service.Repositories;
import com.PIDEV.Course_Service.Entities.Certificate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface CertificateRepository extends JpaRepository<Certificate, Long> {
   Optional<Certificate> findByUserCourseId(Long userCourseId);
}