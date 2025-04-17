package com.megaminds.payment.controller;

import com.megaminds.payment.dto.PaymentRequest;
import com.megaminds.payment.entity.ChargeRequest;
import com.megaminds.payment.service.PaymentService;
import com.megaminds.payment.service.StripeService;
import com.stripe.exception.StripeException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class PaymentController {
    private final StripeService stripeService;

    @PostMapping("/charge")
    public ResponseEntity<Map<String, String>> charge(@RequestBody ChargeRequest chargeRequest) {
        try {
            String clientSecret = stripeService.charge(chargeRequest.getAmount(), chargeRequest.getCurrency(), chargeRequest.getPaymentMethodId());
            return ResponseEntity.ok(Map.of("clientSecret", clientSecret));
        } catch (StripeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

}
