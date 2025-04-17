package com.megaminds.order.material;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;

@Validated

public record PurchaseRequest(
        @NotNull(message = "Product is mandatory")
        Integer materialId,
        @Positive(message = "Quantity is mandatory")
        double quantity

) {
}
