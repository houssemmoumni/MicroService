package com.megaminds.order.dto;

public record OrderLineRequest(

        Integer orderId,
        Integer productId,
        double quantity
) {
}
