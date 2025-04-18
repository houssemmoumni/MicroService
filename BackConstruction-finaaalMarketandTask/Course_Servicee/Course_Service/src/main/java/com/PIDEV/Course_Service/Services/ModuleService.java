package com.PIDEV.Course_Service.Services;
import com.PIDEV.Course_Service.Entities.Module;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ModuleService {


    Module addModuleToCourse(Long courseId, Module module);

    Module addVideoToModule(Long moduleId, MultipartFile videoFile) throws IOException;

    void removeModuleFromCourse(Long moduleId) throws IOException;

    List<Module> getModulesByCourseId(Long courseId); // Récupérer tous les modules d'un cours


    Module updateModule(Long moduleId, Module moduleDetails, MultipartFile videoFile) throws IOException;

    Module getModuleById(Long moduleId);
}