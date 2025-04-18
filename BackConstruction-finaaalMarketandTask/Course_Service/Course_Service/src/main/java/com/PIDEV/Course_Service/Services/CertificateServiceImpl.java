package com.PIDEV.Course_Service.Services;

import com.PIDEV.Course_Service.DTO.UserDTO;
import com.PIDEV.Course_Service.Entities.Certificate;
import com.PIDEV.Course_Service.Entities.UserCourse;
import com.PIDEV.Course_Service.Feign.UserClient;
import com.PIDEV.Course_Service.Repositories.CertificateRepository;
import com.PIDEV.Course_Service.Repositories.UserCourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CertificateServiceImpl implements CertificateService {

    private final CertificateRepository certificateRepository;
    private final UserCourseRepository userCourseRepository;
    private final CloudinaryPdfService cloudinaryService;
    private final UserClient userClient;

    private static final String CERTIFICATES_DIR = "chemin/vers/certificates";

    @Override
    public Certificate generateCertificate(Long userCourseId) {
        // Récupérer l'inscription (UserCourse)
        UserCourse userCourse = userCourseRepository.findById(userCourseId)
                .orElseThrow(() -> new RuntimeException("UserCourse not found"));

        // Récupérer les données de l'utilisateur via Feign Client
        UserDTO user = userClient.getUserById(userCourse.getUserId(), getJwtToken());
        if (user == null) {
            throw new RuntimeException("User not found in user-service");
        }

        // Vérifier si un certificat existe déjà
        Optional<Certificate> existingCertificate = certificateRepository.findByUserCourseId(userCourseId);

        Certificate certificate = existingCertificate.orElseGet(() -> {
            Certificate newCert = new Certificate();
            newCert.setUserCourse(userCourse);
            return newCert;
        });

        // Générer le contenu du certificat
        String validationMessage = "Certificat valide\n"
                + "Nom de l'utilisateur : " + user.getLogin() + "\n"
                + (user.getFirstName() != null ? "Prénom : " + user.getFirstName() + "\n" : "")
                + (user.getLastName() != null ? "Nom : " + user.getLastName() + "\n" : "")
                + "Titre du cours : " + userCourse.getCourse().getTitle() + "\n"
                + "Date d'émission : " + LocalDate.now() + "\n"
                + "Ce certificat a été vérifié et est valide.";

        // Générer le certificat PDF
        String fileName = "certificate_" + userCourseId + "_" + System.currentTimeMillis() + ".pdf";
        String filePath = CERTIFICATES_DIR + File.separator + fileName;

        CertificateGenerator.generateCertificate(
                filePath,
                user.getLogin(),
                userCourse.getCourse().getTitle(),
                validationMessage
        );

        // Upload vers Cloudinary
        String cloudinaryUrl = uploadCertificateToCloudinary(filePath);
        certificate.setCertificateUrl(cloudinaryUrl);

        // Lire et stocker le contenu PDF si nécessaire
        try {
            byte[] pdfBytes = Files.readAllBytes(new File(filePath).toPath());
            certificate.setPdfContent(pdfBytes);
            new File(filePath).delete(); // Supprimer le fichier local après traitement
        } catch (IOException e) {
            throw new RuntimeException("Failed to process PDF file", e);
        }

        return certificateRepository.save(certificate);
    }

    private String getJwtToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
            Jwt jwt = (Jwt) authentication.getPrincipal();
            return "Bearer " + jwt.getTokenValue();
        }
        throw new RuntimeException("No JWT token found in security context");
    }

    private String uploadCertificateToCloudinary(String filePath) {
        try {
            File file = new File(filePath);
            MultipartFile multipartFile = new MockMultipartFile(
                    file.getName(),
                    file.getName(),
                    "application/pdf",
                    Files.readAllBytes(file.toPath())
            );
            return cloudinaryService.uploadPdf(multipartFile);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload certificate to Cloudinary", e);
        }
    }

    @Override
    public Optional<Certificate> getCertificateByUserCourseId(Long userCourseId) {
        return certificateRepository.findByUserCourseId(userCourseId);
    }
}