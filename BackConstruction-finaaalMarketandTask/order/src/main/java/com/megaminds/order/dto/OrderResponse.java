package com.megaminds.order.dto;

import com.megaminds.order.entity.PaymentMethod;

import java.math.BigDecimal;

public record OrderResponse(
        Integer id,
        String reference,
        BigDecimal amount,
        PaymentMethod paymentMethod,
        int customerId
) {
}
