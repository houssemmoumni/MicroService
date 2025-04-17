package com.megaminds.order.material;

import java.math.BigDecimal;

public record PurchaseResponse(Integer materialId,
                               String name,
                               String description,
                               BigDecimal price,
                               double quantity) {
}
