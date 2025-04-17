package com.megaminds.order.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.megaminds.order.entity.PaymentMethod;
import com.megaminds.order.material.PurchaseRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)

public record OrderRequest(Integer id,
                           //String reference,
                           @Positive(message = "Order amount should be positive")
                           BigDecimal amount,
                           @NotNull(message = "Payment method should be precised")
                           PaymentMethod paymentMethod,
                           @NotNull(message = "Customer should be present")
                            int customerId,
                           @NotEmpty(message = "You should at least purchase one material")
                           List<PurchaseRequest> materials) {
}
