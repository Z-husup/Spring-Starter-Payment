package com.example.springstarterpayment.gateway;

import java.util.List;
import java.util.Map;

public interface PaymentGateway {

    PaymentProvider provider();

    PaymentResponse createPayment(PaymentRequest request);

    PaymentStatusResponse getPaymentStatus(String paymentId);

    enum PaymentProvider {
        STRIPE,
        YOOKASSA,
        FREEDOMPAY
    }

    record PaymentRequest(
            long amountCents,
            String currency,
            PaymentType type,
            String description,
            String customerReference,
            List<LineItem> items,
            Map<String,String> metadata,
            String successUrl,
            String cancelUrl
    ) {}

    record LineItem(
            String name,
            long unitAmountCents,
            long quantity
    ) {}

    record PaymentResponse(
            String paymentId,
            String providerReference,
            String redirectUrl
    ) {}

    record PaymentStatusResponse(
            String paymentId,
            PaymentStatus status
    ) {}

    enum PaymentStatus {
        CREATED,
        PENDING,
        PAID,
        FAILED,
        CANCELED
    }

    enum PaymentType {
        ONE_TIME,
        SUBSCRIPTION
    }
}