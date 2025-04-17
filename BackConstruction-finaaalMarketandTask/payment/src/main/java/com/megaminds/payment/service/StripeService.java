package com.megaminds.payment.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class StripeService {
    private final String secretKey;

    public StripeService(@Value("${spring.stripe.secret-key}") String secretKey) {
        this.secretKey = secretKey;
    }



    public String charge(BigDecimal amount, String currency, String paymentMethodId) throws StripeException {
        Stripe.apiKey = secretKey;

        long amountInCents = amount.multiply(BigDecimal.valueOf(100)).longValue();

        // Build PaymentIntent parameters with automatic payment methods
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amountInCents)
                .setCurrency(currency)
                .setPaymentMethod(paymentMethodId)
                .setConfirm(true) // Automatically confirm the payment
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                .setEnabled(true)
                                .setAllowRedirects(PaymentIntentCreateParams.AutomaticPaymentMethods.AllowRedirects.NEVER)
                                .build()
                )
                .build();

        PaymentIntent paymentIntent = PaymentIntent.create(params);
        return paymentIntent.getClientSecret(); // Send this to the frontend
    }
}
