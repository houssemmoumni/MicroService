package com.megaminds.payment.dto;

import com.megaminds.payment.entity.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(Integer id,
                             BigDecimal amount,
                             PaymentMethod paymentMethod,
                             Integer orderId,
                             String orderReference,
                             Customer customer) {
}
