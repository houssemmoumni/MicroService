package com.megaminds.material.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.megaminds.material.dto.*;
import com.megaminds.material.entity.Category;
import com.megaminds.material.service.MaterialService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/v1/materials")
@RequiredArgsConstructor
public class MaterialController {
    private final MaterialService service;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Integer> createMaterial(
            @RequestParam Integer userId,
            @RequestPart("request") String request,
            @RequestPart("file") MultipartFile file
    ) {
        try {
        ObjectMapper objectMapper = new ObjectMapper();
        MaterialRequest requestmat = objectMapper.readValue(request, MaterialRequest.class);

        ImageModel imageModel = new ImageModel();
        imageModel.setName(file.getOriginalFilename());
        imageModel.setFile(file);
        return ResponseEntity.ok(service.createMaterial(userId,requestmat, imageModel));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build(); // Handle JSON parsing errors
        }
    }

    @PostMapping("/purchase")
    public ResponseEntity<List<MaterialPurchaseResponse>> purchaseMaterials(
            @RequestBody List<MaterialPurchaseRequest> request
    ) {
        return ResponseEntity.ok(service.purchaseMaterials(request));
    }

    @GetMapping("/{material-id}")
    public ResponseEntity<MaterialResponse> findById(
            @PathVariable("material-id") Integer materialId
    ) {
        return ResponseEntity.ok(service.findById(materialId));
    }

    @GetMapping
    public ResponseEntity<List<MaterialResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @PutMapping("/{material-id}")
    public ResponseEntity<MaterialResponse> updateMaterial(
            @RequestParam Integer userId,  // Get userId from request param
            @PathVariable("material-id") Integer materialId,
            @RequestBody @Valid MaterialRequest request
    ) {
        return ResponseEntity.ok(service.updateMaterial(userId,materialId, request));
    }

    @DeleteMapping("/{material-id}")
    public ResponseEntity<Void> deleteMaterial(
            @RequestParam Integer userId,  // Get userId from request param
           @PathVariable("material-id") Integer materialId
    ) {
        service.deleteMaterial(userId,materialId);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/categories")
    public List<Category> getAllCategories() {
        return service.getAllCategories();
    }
}
