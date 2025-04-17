package com.megaminds.order.kafka;

import com.megaminds.order.entity.PaymentMethod;
import com.megaminds.order.material.PurchaseResponse;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation(
        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        List<PurchaseResponse> products
) {

}
