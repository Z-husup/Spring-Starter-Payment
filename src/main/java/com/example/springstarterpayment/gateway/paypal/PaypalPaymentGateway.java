package com.example.springstarterpayment.gateway.paypal;

import com.example.springstarterpayment.gateway.AbstractHttpPaymentGateway;
import com.example.springstarterpayment.gateway.PaymentGateway;
import com.example.springstarterpayment.exception.PaymentIntegrationException;
import com.example.springstarterpayment.properties.paypal.PaypalProperties;
import tools.jackson.databind.JsonNode;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;

public class PaypalPaymentGateway extends AbstractHttpPaymentGateway {

    @Override
    public PaymentProvider provider() {
        return PaymentProvider.PAYPAL;
    }

    private final PaypalProperties properties;

    public PaypalPaymentGateway(PaypalProperties properties) {
        super(properties.getConnectTimeout());
        this.properties = properties;
    }

    @Override
    public PaymentResponse createPayment(PaymentRequest request) {

        try {

            String token = obtainAccessToken();

            Map<String,Object> order = Map.of(
                    "intent","CAPTURE",
                    "purchase_units", List.of(
                            Map.of(
                                    "amount", Map.of(
                                            "currency_code", properties.getCurrency(),
                                            "value", String.valueOf(request.amountCents()/100.0)
                                    )
                            )
                    ),
                    "application_context", Map.of(
                            "return_url", request.successUrl(),
                            "cancel_url", request.cancelUrl()
                    )
            );

            String body = objectMapper.writeValueAsString(order);

            String response = sendRequest(
                    properties.getApiBase()+"/v2/checkout/orders",
                    "POST",
                    body,
                    Map.of(
                            "Authorization","Bearer "+token,
                            "Content-Type","application/json"
                    )
            );

            tools.jackson.databind.JsonNode json = objectMapper.readTree(response);

            for(JsonNode link : json.path("links")){

                if("approve".equals(link.path("rel").asText())){

                    return new PaymentResponse(
                            json.path("id").asText(),
                            json.path("id").asText(),
                            link.path("href").asText()
                    );

                }
            }

            throw new PaymentIntegrationException("PayPal approve url not found");

        } catch (Exception ex) {
            throw new PaymentIntegrationException("PayPal create payment error",ex);
        }
    }

    @Override
    public PaymentStatusResponse getPaymentStatus(String paymentId) {

        String token;

        try {
            token = obtainAccessToken();
        } catch (Exception ex){
            throw new PaymentIntegrationException("PayPal token error",ex);
        }

        String response = sendRequest(
                properties.getApiBase()+"/v2/checkout/orders/"+encode(paymentId),
                "GET",
                null,
                Map.of("Authorization","Bearer "+token)
        );

        try{

            tools.jackson.databind.JsonNode json = objectMapper.readTree(response);

            String status = json.path("status").asText();

            PaymentStatus mapped = switch (status) {
                case "COMPLETED" -> PaymentStatus.PAID;
                case "APPROVED" -> PaymentStatus.PENDING;
                case "VOIDED","CANCELLED" -> PaymentStatus.CANCELED;
                default -> PaymentStatus.CREATED;
            };

            return new PaymentStatusResponse(paymentId,mapped);

        }catch(Exception ex){
            throw new PaymentIntegrationException("PayPal parse error",ex);
        }
    }

    private String obtainAccessToken() {

        String auth =
                Base64.getEncoder().encodeToString(
                        (properties.getClientId()+":"+properties.getClientSecret())
                                .getBytes(StandardCharsets.UTF_8));

        String response = sendRequest(
                properties.getApiBase()+"/v1/oauth2/token",
                "POST",
                "grant_type=client_credentials",
                Map.of(
                        "Authorization","Basic "+auth,
                        "Content-Type","application/x-www-form-urlencoded"
                )
        );

        try{

            tools.jackson.databind.JsonNode json = objectMapper.readTree(response);

            return json.path("access_token").asText();

        }catch(Exception ex){
            throw new PaymentIntegrationException("PayPal token parse error",ex);
        }
    }
}