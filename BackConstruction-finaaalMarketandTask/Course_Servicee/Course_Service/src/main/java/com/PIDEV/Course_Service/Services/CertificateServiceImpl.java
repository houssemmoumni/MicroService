package com.PIDEV.Course_Service.Services;

import com.PIDEV.Course_Service.Entities.Certificate;
import com.PIDEV.Course_Service.Entities.UserCourse;
import com.PIDEV.Course_Service.Repositories.CertificateRepository;
import com.PIDEV.Course_Service.Repositories.UserCourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.PIDEV.Course_Service.Services.CertificateGenerator;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class CertificateServiceImpl implements CertificateService {

    @Autowired
    private CertificateRepository certificateRepository;

    @Autowired
    private UserCourseRepository userCourseRepository;

    @Autowired
    private CloudinaryPdfService cloudinaryService; // Injectez CloudinaryService

    private static final String CERTIFICATES_DIR = "C:\\Users\\compuserv plus\\Downloads\\BackConstruction-main\\BackConstruction-main\\Course_Service\\Course_Service\\src\\main\\resources\\certificates";

    @Override
    public Certificate generateCertificate(Long userCourseId) {
        // Récupérer l'inscription (UserCourse)
        UserCourse userCourse = userCourseRepository.findById(userCourseId)
                .orElseThrow(() -> new RuntimeException("UserCourse not found"));

        // Vérifier si un certificat existe déjà pour ce userCourseId
        Optional<Certificate> existingCertificate = certificateRepository.findByUserCourseId(userCourseId);

        Certificate certificate;
        if (existingCertificate.isPresent()) {
            // Mettre à jour le certificat existant
            certificate = existingCertificate.get();
        } else {
            // Créer un nouveau certificat
            certificate = new Certificate();
            certificate.setUserCourse(userCourse);
        }

        // Générer le texte du QR code (URL de validation)
        // Générer le texte du QR code (informations du certificat)
        // Générer une phrase de validation
        String validationMessage = "Certificat valide\n"
                + "Nom de l'utilisateur : " + userCourse.getUser().getUsername() + "\n"
                + "Titre du cours : " + userCourse.getCourse().getTitle() + "\n"
                + "Date d'émission : " + LocalDate.now() + "\n"
                + "Ce certificat a été vérifié et est valide.";

// Générer le texte du QR code (phrase de validation)
        String qrCodeText = validationMessage;


        // Générer le certificat avec le QR code
        String fileName = "certificate_" + userCourseId + ".pdf";
        String filePath = CERTIFICATES_DIR + File.separator + fileName;
        CertificateGenerator.generateCertificate(
                filePath,
                userCourse.getUser().getUsername(),
                userCourse.getCourse().getTitle(),
                qrCodeText // Ajoutez ce quatrième argument
        );

        // Lire le contenu du fichier PDF
        byte[] pdfBytes;
        try {
            pdfBytes = Files.readAllBytes(new File(filePath).toPath());
        } catch (IOException e) {
            throw new RuntimeException("Failed to read PDF file", e);
        }

        // Mettre à jour le contenu du PDF dans l'objet Certificate
        certificate.setPdfContent(pdfBytes);

        // Enregistrer ou mettre à jour le certificat dans la base de données
        return certificateRepository.save(certificate);
    }

    private String uploadCertificateToCloudinary(String filePath) {
        try {
            File file = new File(filePath);
            MultipartFile multipartFile = new MockMultipartFile(
                    file.getName(), // Nom du fichier
                    file.getName(), // Nom original
                    "application/pdf", // Type MIME
                    Files.readAllBytes(file.toPath()) // Contenu du fichier
            );

            // Utilisez CloudinaryService pour téléverser le fichier
            String certificateUrl = cloudinaryService.uploadPdf(multipartFile);

            // Supprimez le fichier local après le téléversement
            file.delete();

            return certificateUrl; // Retourne l'URL publique du fichier
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload certificate to Cloudinary", e);
        }
    }

    @Override
    public Optional<Certificate> getCertificateByUserCourseId(Long userCourseId) {
        return certificateRepository.findByUserCourseId(userCourseId);
    }
}