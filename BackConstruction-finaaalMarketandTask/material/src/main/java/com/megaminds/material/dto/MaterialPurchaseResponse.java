package com.megaminds.material.dto;

import java.math.BigDecimal;

public record MaterialPurchaseResponse(
        Integer materialId,
        String name,
        String description,
        BigDecimal price,
        double quantity
) {
}
