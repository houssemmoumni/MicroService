package com.megaminds.notification.kafka.order;

import java.math.BigDecimal;

public record PurchaseResponse(Integer materialId,
                               String name,
                               String description,
                               BigDecimal price,
                               double quantity) {
}
