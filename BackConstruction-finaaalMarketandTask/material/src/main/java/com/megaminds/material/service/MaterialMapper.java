package com.megaminds.material.service;

import com.megaminds.material.dto.MaterialPurchaseResponse;
import com.megaminds.material.dto.MaterialRequest;
import com.megaminds.material.dto.MaterialResponse;
import com.megaminds.material.entity.Category;
import com.megaminds.material.entity.Material;
import com.megaminds.material.entity.MaterialStatus;
import com.megaminds.material.entity.User;
import org.springframework.stereotype.Service;

@Service
public class MaterialMapper {
    public Material toMaterial(MaterialRequest request, int userId) {
        return Material.builder()
                .id(request.id())
                .name(request.name())
                .description(request.description())
                .availableQuantity(request.availableQuantity())
                .price(request.price())
                .category(
                        Category.builder()
                                .id(request.categoryId())
                                .build()
                )
                .status(MaterialStatus.valueOf(request.status()))
                .createdBy(userId)// Set the createdBy user
                .build();
    }
    public MaterialResponse toMaterialResponse(Material material) {
        return new MaterialResponse(
                material.getId(),
                material.getName(),
                material.getDescription(),
                material.getAvailableQuantity(),
                material.getPrice(),
                material.getCategory().getId(),
                material.getCategory().getName(),
                material.getCategory().getDescription(),
                material.getStatus().name(),
                material.getImage()
        );
    }
    public MaterialPurchaseResponse toMaterialPurchaseResponse(Material material, double quantity) {
        return new MaterialPurchaseResponse(
                material.getId(),
                material.getName(),
                material.getDescription(),
                material.getPrice(),
                quantity
        );
    }
}
