package com.example.springstarterpayment.gateway.freedom_pay;

import com.example.springstarterpayment.gateway.AbstractHttpPaymentGateway;
import com.example.springstarterpayment.properties.freedom_pay.FreedomPayProperties;

import java.util.UUID;

public class FreedomPayPaymentGateway extends AbstractHttpPaymentGateway {

    @Override
    public PaymentProvider provider() {
        return PaymentProvider.FREEDOMPAY;
    }

    private final FreedomPayProperties properties;

    public FreedomPayPaymentGateway(FreedomPayProperties properties) {
        super(properties.getConnectTimeout());
        this.properties = properties;
    }

    @Override
    public PaymentResponse createPayment(PaymentRequest request) {

        String redirectUrl =
                properties.getApiBase() +
                        "/pay?merchant_id=" + properties.getMerchantId() +
                        "&amount=" + request.amountCents()/100.0 +
                        "&currency=" + properties.getCurrency();

        return new PaymentResponse(
                UUID.randomUUID().toString(),
                null,
                redirectUrl
        );
    }

    @Override
    public PaymentStatusResponse getPaymentStatus(String paymentId) {

        return new PaymentStatusResponse(paymentId, PaymentStatus.PENDING);
    }
}