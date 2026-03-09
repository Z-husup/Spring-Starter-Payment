package com.example.springstarterpayment.gateway;

import com.example.springstarterpayment.exception.PaymentIntegrationException;
import com.example.springstarterpayment.properties.PaymentProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.*;
import java.nio.charset.StandardCharsets;

public class StripePaymentGateway implements PaymentGateway {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final PaymentProperties properties;

    public StripePaymentGateway(PaymentProperties properties) {

        this.properties = properties;

        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(
                        java.time.Duration.ofMillis(
                                properties.getStripe().getConnectTimeout()))
                .build();
    }

    @Override
    public PaymentResponse createPayment(PaymentRequest request) {

        String secretKey = properties.getStripe().getSecretKey();

        if(secretKey == null || secretKey.isBlank()){
            throw new PaymentIntegrationException("Stripe secret key not configured");
        }

        String currency = request.currency() != null
                ? request.currency()
                : properties.getStripe().getDefaultCurrency();

        String mode = request.type() == PaymentType.SUBSCRIPTION
                ? "subscription"
                : "payment";

        StringBuilder body = new StringBuilder();

        append(body,"payment_method_types[0]","card");
        append(body,"mode",mode);
        append(body,"success_url",request.successUrl());
        append(body,"cancel_url",request.cancelUrl());
        append(body,"client_reference_id",request.customerReference());

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

        String response = sendStripeRequest(
                properties.getStripe().getApiBase() + "/v1/checkout/sessions",
                "POST",
                body.toString()
        );

        try {

            JsonNode json = objectMapper.readTree(response);

            return new PaymentResponse(
                    json.path("id").asText(),
                    json.path("id").asText(),
                    json.path("url").asText()
            );

        } catch(Exception ex){
            throw new PaymentIntegrationException("Stripe response parse error",ex);
        }
    }

    @Override
    public PaymentStatusResponse getPaymentStatus(String paymentId) {

        String response = sendStripeRequest(
                properties.getStripe().getApiBase() + "/v1/checkout/sessions/" + encode(paymentId),
                "GET",
                null
        );

        try {

            JsonNode json = objectMapper.readTree(response);

            String stripeStatus = json.path("payment_status").asText();

            PaymentStatus status = switch (stripeStatus) {
                case "paid" -> PaymentStatus.PAID;
                case "unpaid" -> PaymentStatus.PENDING;
                default -> PaymentStatus.CREATED;
            };

            return new PaymentStatusResponse(paymentId,status);

        } catch(Exception ex){
            throw new PaymentIntegrationException("Stripe session parse error",ex);
        }
    }

    private String sendStripeRequest(String url,String method,String body){

        try{

            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization",
                            "Bearer " + properties.getStripe().getSecretKey());

            if("POST".equalsIgnoreCase(method)){

                builder.header("Content-Type",
                        "application/x-www-form-urlencoded");

                builder.POST(HttpRequest.BodyPublishers.ofString(body));

            }else{
                builder.GET();
            }

            HttpResponse<String> response =
                    httpClient.send(builder.build(),
                            HttpResponse.BodyHandlers.ofString());

            if(response.statusCode()<200 || response.statusCode()>=300){
                throw new PaymentIntegrationException(
                        "Stripe error "+response.statusCode()+" "+response.body());
            }

            return response.body();

        }catch(Exception ex){
            throw new PaymentIntegrationException("Stripe request failed",ex);
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

    private String encode(String value){
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}