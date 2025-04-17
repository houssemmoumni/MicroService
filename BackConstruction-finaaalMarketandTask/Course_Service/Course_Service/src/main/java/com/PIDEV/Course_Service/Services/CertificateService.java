package com.PIDEV.Course_Service.Services;

import com.PIDEV.Course_Service.Entities.Certificate;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CertificateService {
    Certificate generateCertificate(Long userCourseId); // Générer un certificat pour un UserCourse
    Optional<Certificate> getCertificateByUserCourseId(Long userCourseId); // Récupérer un certificat par UserCourse ID
}