package com.PIDEV.Course_Service.Services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService(
            @Value("${cloudinary.cloud-name}") String cloudName,
            @Value("${cloudinary.api-key}") String apiKey,
            @Value("${cloudinary.api-secret}") String apiSecret) {
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret
        ));
    }

    // Méthode pour uploader une vidéo
    public String uploadVideo(MultipartFile file) throws IOException {
        Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                "resource_type", "video" // Spécifiez que c'est une vidéo
        ));
        return (String) uploadResult.get("url"); // Retourne l'URL de la vidéo
    }

    // Méthode pour supprimer une vidéo
    public void deleteVideo(String publicId) throws IOException {
        cloudinary.uploader().destroy(publicId, ObjectUtils.asMap(
                "resource_type", "video"
        ));
    }
}