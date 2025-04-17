package com.PIDEV.Course_Service.Services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryPdfService {

    private final Cloudinary cloudinary;

    public CloudinaryPdfService(
            @Value("${cloudinary.cloud-name}") String cloudName,
            @Value("${cloudinary.api-key}") String apiKey,
            @Value("${cloudinary.api-secret}") String apiSecret) {
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret
        ));
    }

    // Méthode pour uploader un fichier PDF
    public String uploadPdf(MultipartFile file) throws IOException {
        Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                "resource_type", "raw" // Spécifiez "raw" pour les fichiers non multimédias
        ));
        return (String) uploadResult.get("url"); // Retourne l'URL du fichier
    }

    // Méthode pour supprimer un fichier PDF
    public void deletePdf(String publicId) throws IOException {
        cloudinary.uploader().destroy(publicId, ObjectUtils.asMap(
                "resource_type", "raw"
        ));
    }
}