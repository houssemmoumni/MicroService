package com.megaminds.payment.dto;

import com.megaminds.payment.entity.PaymentMethod;

import java.math.BigDecimal;

public record PaymentNotificationRequest(String orderReference,
                                         BigDecimal amount,
                                         PaymentMethod paymentMethod,
                                         String customerFirstname,
                                         String customerLastname,
                                         String customerEmail) {
}
