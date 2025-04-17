package com.megaminds.material.service;

import com.megaminds.material.dto.*;
import com.megaminds.material.entity.Category;
import com.megaminds.material.entity.Material;
import com.megaminds.material.entity.MaterialStatus;
import com.megaminds.material.entity.User;
import com.megaminds.material.exception.MaterialPurchaseException;
import com.megaminds.material.repository.CategoryRepository;
import com.megaminds.material.repository.MaterialRepository;
import com.megaminds.material.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MaterialService {
    private final MaterialRepository repository;
    private final CategoryRepository categoryRepository;
    private final MaterialMapper mapper;
    private final ImageService imageService;

    private static final Integer ADMIN_USER_ID = 1; // Replace with your admin's userId


    public Integer createMaterial(Integer userId,
            MaterialRequest request, ImageModel model
    ){
        if (!userId.equals(ADMIN_USER_ID)) {
            throw new RuntimeException("Access denied: Only admins can create materials");
        }

        var material = mapper.toMaterial(request, userId);
        imageService.uploadImage(material, model);
        return repository.save(material).getId();

    }
    public MaterialResponse findById(Integer id) {
        return repository.findById(id)
                .map(mapper::toMaterialResponse)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID:: " + id));
    }

    public List<MaterialResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toMaterialResponse)
                .collect(Collectors.toList());
    }

   // @Transactional(rollbackFor = MaterialPurchaseException.class)
    public List<MaterialPurchaseResponse> purchaseMaterials(
            List<MaterialPurchaseRequest> request
    ) {
        var materialIds = request
                .stream()
                .map(MaterialPurchaseRequest::materialId)
                .toList();
        var storedMaterials = repository.findAllByIdInOrderById(materialIds);
        if (materialIds.size() != storedMaterials.size()) {
            throw new MaterialPurchaseException("One or more products does not exist");
        }
        var sortedRequest = request
                .stream()
                .sorted(Comparator.comparing(MaterialPurchaseRequest::materialId))
                .toList();
        var purchasedMaterials = new ArrayList<MaterialPurchaseResponse>();
        for (int i = 0; i < storedMaterials.size(); i++) {
            var material = storedMaterials.get(i);
            var materialRequest = sortedRequest.get(i);
            if (material.getAvailableQuantity() < materialRequest.quantity()) {
                throw new MaterialPurchaseException("Insufficient stock quantity for product with ID:: " + materialRequest.materialId());
            }
            var newAvailableQuantity = material.getAvailableQuantity() - materialRequest.quantity();
            material.setAvailableQuantity(newAvailableQuantity);
            repository.save(material);
            purchasedMaterials.add(mapper.toMaterialPurchaseResponse(material, materialRequest.quantity()));
        }
        return purchasedMaterials;
    }
    public MaterialResponse updateMaterial(Integer userId,Integer materialId, MaterialRequest request) {

        Material material = repository.findById(materialId)
                .orElseThrow(() -> new RuntimeException("Material not found"));

        // Check if the user is the admin or the creator of the material
        if (!userId.equals(ADMIN_USER_ID)){
            if (material.getCreatedBy()!=userId) {
                throw new RuntimeException("Access denied: Only admins or the creator can update materials");
            }
        }

        // Find the category by ID
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // Update the material fields
        material.setName(request.name());
        material.setDescription(request.description());
        material.setAvailableQuantity(request.availableQuantity());
        material.setPrice(request.price());
        material.setCategory(category);
        material.setStatus(MaterialStatus.valueOf(request.status()));

        // Save the updated material
        Material updatedMaterial = repository.save(material);

        // Return the updated material as a MaterialResponse
        return new MaterialResponse(
                updatedMaterial.getId(),
                updatedMaterial.getName(),
                updatedMaterial.getDescription(),
                updatedMaterial.getAvailableQuantity(),
                updatedMaterial.getPrice(),
                updatedMaterial.getCategory().getId(),
                updatedMaterial.getCategory().getName(),
                updatedMaterial.getCategory().getDescription(),
                updatedMaterial.getStatus().name(),
                updatedMaterial.getImage()
        );

    }
    public void deleteMaterial(Integer userId,Integer materialId) {

        Material material = repository.findById(materialId)
                .orElseThrow(() -> new RuntimeException("Material not found"));

        // Check if the user is the admin or the creator of the material
        if (!userId.equals(ADMIN_USER_ID)) {
            if (material.getCreatedBy()!=userId) {
                throw new RuntimeException("Access denied: Only admins or the creator can delete materials");
            }
        }

        // Delete the material
        repository.deleteById(materialId);
    }
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}
