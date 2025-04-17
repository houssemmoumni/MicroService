package com.PIDEV.Course_Service.Controllers;

import com.PIDEV.Course_Service.Entities.Module;
import com.PIDEV.Course_Service.Services.ModuleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


import jakarta.validation.Valid;



@Validated
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/modules")
public class ModuleController {

    @Autowired
    private ModuleServiceImpl moduleService;

    @PostMapping(value = "/add/{courseId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Module> addModuleToCourse(
            @PathVariable Long courseId,
            @RequestBody @Valid Module module) {
        Module savedModule = moduleService.addModuleToCourse(courseId, module);
        return ResponseEntity.ok(savedModule);
    }


    @PostMapping(value = "/add/{moduleId}/video", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Module> addVideoToModule(
            @PathVariable Long moduleId,
            @RequestParam("videoFile") MultipartFile videoFile) throws IOException {
        Module updatedModule = moduleService.addVideoToModule(moduleId, videoFile);
        return ResponseEntity.ok(updatedModule);
    }
    // Supprimer un module d'un cours
    @DeleteMapping("/remove/{moduleId}")
    public ResponseEntity<Void> removeModuleFromCourse(@PathVariable Long moduleId) throws IOException {
        moduleService.removeModuleFromCourse(moduleId);
        return ResponseEntity.noContent().build();
    }

    // Récupérer tous les modules d'un cours
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Module>> getModulesByCourseId(@PathVariable Long courseId) {
        List<Module> modules = moduleService.getModulesByCourseId(courseId);
        return ResponseEntity.ok(modules);
    }

    // Mettre à jour un module avec une vidéo
    @PutMapping("/update/{moduleId}")
    public ResponseEntity<Module> updateModule(
            @PathVariable Long moduleId,
            @RequestPart("module") Module moduleDetails,
            @RequestPart(value = "videoFile", required = false) MultipartFile videoFile) throws IOException {
        Module updatedModule = moduleService.updateModule(moduleId, moduleDetails, videoFile);
        return ResponseEntity.ok(updatedModule);
    }

    // Récupérer un module par son ID
    @GetMapping("/{moduleId}")
    public ResponseEntity<Module> getModuleById(@PathVariable Long moduleId) {
        Module module = moduleService.getModuleById(moduleId);
        return ResponseEntity.ok(module);
    }
}