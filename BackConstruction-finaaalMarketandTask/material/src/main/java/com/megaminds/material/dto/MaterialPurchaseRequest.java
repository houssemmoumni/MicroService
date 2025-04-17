package com.megaminds.material.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record MaterialPurchaseRequest(
        @NotNull(message = "Material is mandatory")
        Integer materialId,
        @Positive(message = "Quantity is mandatory")
        double quantity
) {
}
