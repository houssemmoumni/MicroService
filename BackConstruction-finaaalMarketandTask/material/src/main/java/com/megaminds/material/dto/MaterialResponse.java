package com.megaminds.material.dto;

import com.megaminds.material.entity.Image;

import java.math.BigDecimal;

public record MaterialResponse(
        Integer id,
        String name,
        String description,
        double availableQuantity,
        BigDecimal price,
        Integer categoryId,
        String categoryName,
        String categoryDescription,
        String status,
        Image image
) {
}
