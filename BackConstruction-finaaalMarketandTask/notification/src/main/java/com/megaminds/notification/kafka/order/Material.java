package com.megaminds.notification.kafka.order;

import java.math.BigDecimal;

public record Material(
        Integer materialId,
        String name,
        String description,
        BigDecimal price,
        double quantity
) {
}
