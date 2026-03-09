package com.example.springstarterpayment.gateway.yoo_kassa;

import com.example.springstarterpayment.exception.PaymentIntegrationException;
import com.example.springstarterpayment.gateway.AbstractHttpPaymentGateway;
import com.example.springstarterpayment.properties.yoo_kassa.YooKassaProperties;
import tools.jackson.databind.JsonNode;

import java.util.Base64;
import java.util.Map;

public class YooKassaPaymentGateway extends AbstractHttpPaymentGateway {

    @Override
    public PaymentProvider provider() {
        return PaymentProvider.YOOKASSA;
    }

    private final YooKassaProperties properties;

    public YooKassaPaymentGateway(YooKassaProperties properties) {
        super(properties.getConnectTimeout());
        this.properties = properties;
    }

    @Override
    public PaymentResponse createPayment(PaymentRequest request) {

        Map<String,Object> payload = Map.of(
                "amount", Map.of(
                        "value", request.amountCents() / 100.0,
                        "currency", properties.getDefaultCurrency()
                ),
                "confirmation", Map.of(
                        "type","redirect",
                        "return_url",request.successUrl()
                ),
                "description", request.description()
        );

        try{

            String body = objectMapper.writeValueAsString(payload);

            String response = sendRequest(
                    properties.getApiBase()+"/payments",
                    "POST",
                    body,
                    Map.of(
                            "Authorization",
                            "Basic "+ Base64.getEncoder().encodeToString(
                                    (properties.getShopId()+":"+properties.getSecretKey()).getBytes()),
                            "Content-Type","application/json"
                    )
            );

            JsonNode json = objectMapper.readTree(response);

            return new PaymentResponse(
                    json.path("id").asText(),
                    json.path("id").asText(),
                    json.path("confirmation").path("confirmation_url").asText()
            );

        }catch(Exception ex){
            throw new PaymentIntegrationException("YooKassa error",ex);
        }
    }

    @Override
    public PaymentStatusResponse getPaymentStatus(String paymentId) {

        return new PaymentStatusResponse(paymentId, PaymentStatus.PENDING);
    }
}