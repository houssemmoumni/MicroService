package com.PIDEV.Course_Service.Controllers;

import com.PIDEV.Course_Service.Entities.Certificate;
import com.PIDEV.Course_Service.Services.CertificateServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200") // Autorise les requêtes depuis Angular
@RequestMapping("/api/certificates") // Base URL pour ce contrôleur
public class CertificateController {

    @Autowired
    private CertificateServiceImpl certificateService;

    // Endpoint pour générer un certificat
    @PostMapping("/generate")
    public ResponseEntity<byte[]> generateCertificate(
            @RequestParam Long userCourseId // ID de l'inscription (UserCourse)
    ) {
        // Générer le certificat
        Certificate certificate = certificateService.generateCertificate(userCourseId);

        // Récupérer le contenu binaire du PDF
        byte[] pdfBytes = certificate.getPdfContent();

        // Définir les en-têtes HTTP
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF); // Type MIME du PDF
        headers.setContentDispositionFormData("attachment", "certificat.pdf"); // Forcer le téléchargement

        // Renvoyer le fichier PDF
        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.CREATED);
    }

    // Endpoint pour récupérer un certificat par UserCourse ID
    @GetMapping("/{userCourseId}")
    public ResponseEntity<Certificate> getCertificateByUserCourseId(
            @PathVariable Long userCourseId // ID de l'inscription (UserCourse)
    ) {
        Optional<Certificate> certificate = certificateService.getCertificateByUserCourseId(userCourseId);
        return certificate.map(ResponseEntity::ok) // Retourne 200 (OK) si le certificat existe
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND)); // Retourne 404 (Not Found) sinon
    }

    // Endpoint pour valider un certificat
    @GetMapping("/validate/{userCourseId}")
    public ResponseEntity<String> validateCertificate(@PathVariable Long userCourseId) {
        Optional<Certificate> certificate = certificateService.getCertificateByUserCourseId(userCourseId);
        if (certificate.isPresent()) {
            return ResponseEntity.ok("Le certificat est valide.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Certificat non trouvé ou invalide.");
        }
    }
}