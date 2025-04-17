package com.megaminds.notification.kafka.order;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation(
        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        List<PurchaseResponse
                > products
) {
}
