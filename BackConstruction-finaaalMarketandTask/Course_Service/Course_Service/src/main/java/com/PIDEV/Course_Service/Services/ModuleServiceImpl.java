package com.PIDEV.Course_Service.Services;

import com.PIDEV.Course_Service.Entities.Course;
import com.PIDEV.Course_Service.Entities.Module;
import com.PIDEV.Course_Service.Repositories.ModuleRepository;
import com.PIDEV.Course_Service.Repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ModuleServiceImpl implements ModuleService {

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Override
    public Module addModuleToCourse(Long courseId, Module module) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        module.setCourse(course);
        return moduleRepository.save(module);
    }



    @Override
    public Module addVideoToModule(Long moduleId, MultipartFile videoFile) throws IOException {
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new RuntimeException("Module not found"));

        // Upload de la vidéo sur Cloudinary
        String videoUrl = cloudinaryService.uploadVideo(videoFile);
        module.setVideoUrl(videoUrl); // Associer l'URL de la vidéo au module

        return moduleRepository.save(module);
    }

    @Override
    public void removeModuleFromCourse(Long moduleId) throws IOException {
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new RuntimeException("Module not found"));

        // Supprimer la vidéo de Cloudinary
        if (module.getVideoUrl() != null) {
            String publicId = extractPublicIdFromUrl(module.getVideoUrl());
            cloudinaryService.deleteVideo(publicId);
        }

        moduleRepository.deleteById(moduleId);
    }

    @Override
    public List<Module> getModulesByCourseId(Long courseId) {
        return moduleRepository.findByCourseId(courseId);
    }

    @Override
    public Module updateModule(Long moduleId, Module moduleDetails, MultipartFile videoFile) throws IOException {
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new RuntimeException("Module not found"));

        // Mettre à jour les champs du module
        module.setTitle(moduleDetails.getTitle());
        module.setContent(moduleDetails.getContent());
        module.setOrderInCourse(moduleDetails.getOrderInCourse());

        // Si une nouvelle vidéo est fournie, la mettre à jour
        if (videoFile != null && !videoFile.isEmpty()) {
            // Supprimer l'ancienne vidéo de Cloudinary
            if (module.getVideoUrl() != null) {
                String publicId = extractPublicIdFromUrl(module.getVideoUrl());
                cloudinaryService.deleteVideo(publicId);
            }

            // Uploader la nouvelle vidéo
            String newVideoUrl = cloudinaryService.uploadVideo(videoFile);
            module.setVideoUrl(newVideoUrl);
        }

        return moduleRepository.save(module);
    }

    @Override
    public Module getModuleById(Long moduleId) {
        return moduleRepository.findById(moduleId)
                .orElseThrow(() -> new RuntimeException("Module not found"));
    }

    // Méthode pour extraire l'ID public d'une URL Cloudinary
    private String extractPublicIdFromUrl(String url) {
        // Exemple d'URL Cloudinary : https://res.cloudinary.com/cloud_name/video/upload/v123456789/public_id.mp4
        String[] parts = url.split("/");
        String publicIdWithExtension = parts[parts.length - 1]; // Récupère "public_id.mp4"
        return publicIdWithExtension.split("\\.")[0]; // Retourne "public_id"
    }
}