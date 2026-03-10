package com.example.springstarterpayment.gateway.stripe;

import com.example.springstarterpayment.exception.PaymentIntegrationException;
import com.example.springstarterpayment.gateway.AbstractHttpPaymentGateway;
import com.example.springstarterpayment.properties.stripe.StripeProperties;
import tools.jackson.databind.JsonNode;

import java.net.http.*;
import java.util.Map;

public class StripePaymentGateway extends AbstractHttpPaymentGateway {

    @Override
    public PaymentProvider provider() {
        return PaymentProvider.STRIPE;
    }

    private final StripeProperties properties;

    public StripePaymentGateway(StripeProperties properties) {
        super(properties.getConnectTimeout());
        this.properties = properties;
    }

    @Override
    public PaymentResponse createPayment(PaymentRequest request) {

        String currency = request.currency() != null
                ? request.currency()
                : properties.getDefaultCurrency();

        String mode = request.type() == PaymentType.SUBSCRIPTION
                ? "subscription"
                : "payment";

        StringBuilder body = new StringBuilder();

        append(body,"payment_method_types[0]","card");
        append(body,"mode",mode);
        append(body,"success_url",request.successUrl());
        append(body,"cancel_url",request.cancelUrl());

        for(int i=0;i<request.items().size();i++){

            LineItem item = request.items().get(i);

            append(body,"line_items["+i+"][price_data][currency]",currency);
            append(body,"line_items["+i+"][price_data][unit_amount]",
                    String.valueOf(item.unitAmountCents()));
            append(body,"line_items["+i+"][price_data][product_data][name]",
                    item.name());
            append(body,"line_items["+i+"][quantity]",
                    String.valueOf(item.quantity()));
        }

        String response = sendRequest(
                properties.getApiBase()+"/v1/checkout/sessions",
                "POST",
                body.toString(),
                Map.of("Authorization","Bearer "+properties.getSecretKey())
        );

        try{

            JsonNode json = objectMapper.readTree(response);

            return new PaymentResponse(
                    json.path("id").asText(),
                    json.path("id").asText(),
                    json.path("url").asText()
            );

        }catch(Exception ex){
            throw new PaymentIntegrationException("Stripe parse error",ex);
        }
    }

    @Override
    public PaymentStatusResponse getPaymentStatus(String paymentId) {

        String response = sendRequest(
                properties.getApiBase()+"/v1/checkout/sessions/"+encode(paymentId),
                "GET",
                null,
                Map.of("Authorization","Bearer "+properties.getSecretKey())
        );

        try{

            tools.jackson.databind.JsonNode json = objectMapper.readTree(response);

            String stripeStatus = json.path("payment_status").asText();

            PaymentStatus status = switch(stripeStatus){
                case "paid" -> PaymentStatus.PAID;
                case "unpaid" -> PaymentStatus.PENDING;
                default -> PaymentStatus.CREATED;
            };

            return new PaymentStatusResponse(paymentId,status);

        }catch(Exception ex){
            throw new PaymentIntegrationException("Stripe parse error",ex);
        }
    }

    private void append(StringBuilder body,String key,String value){

        if(body.length()>0){
            body.append("&");
        }

        body.append(encode(key))
                .append("=")
                .append(encode(value));
    }
}