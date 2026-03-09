package com.example.springstarterpayment.gateway;

import com.example.springstarterpayment.exception.PaymentIntegrationException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class StripePaymentGateway implements PaymentGateway {

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String secretKey;

    public StripePaymentGateway(String secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public CheckoutSession createCheckoutSession(CreateCheckoutSessionCommand command) {

        if (secretKey == null || secretKey.isBlank()) {
            throw new PaymentIntegrationException("Stripe secret key not configured");
        }

        StringBuilder body = new StringBuilder();

        append(body,"payment_method_types[0]","card");
        append(body,"mode","payment");
        append(body,"success_url",command.successUrl());
        append(body,"cancel_url",command.cancelUrl());
        append(body,"client_reference_id",command.clientReferenceId());

        for(int i=0;i<command.lineItems().size();i++){

            LineItem item = command.lineItems().get(i);

            append(body,"line_items["+i+"][price_data][currency]",command.currency());
            append(body,"line_items["+i+"][price_data][unit_amount]",
                    String.valueOf(item.unitAmountCents()));
            append(body,"line_items["+i+"][price_data][product_data][name]",
                    item.name());

            if(item.imageUrl()!=null && !item.imageUrl().isBlank()){
                append(body,"line_items["+i+"][price_data][product_data][images][0]",
                        item.imageUrl());
            }

            append(body,"line_items["+i+"][quantity]",
                    String.valueOf(item.quantity()));
        }

        String response = sendStripeRequest(
                "https://api.stripe.com/v1/checkout/sessions",
                "POST",
                body.toString()
        );

        try {

            JsonNode json = objectMapper.readTree(response);

            return new CheckoutSession(
                    json.path("id").asText(),
                    json.path("url").asText()
            );

        } catch(Exception ex){
            throw new PaymentIntegrationException("Stripe response parse error",ex);
        }
    }

    @Override
    public PaymentSession getSession(String sessionId) {

        String response = sendStripeRequest(
                "https://api.stripe.com/v1/checkout/sessions/"+encode(sessionId),
                "GET",
                null
        );

        try {

            JsonNode json = objectMapper.readTree(response);

            return new PaymentSession(
                    json.path("id").asText(),
                    json.path("payment_status").asText(),
                    json.path("client_reference_id").asText()
            );

        } catch(Exception ex){
            throw new PaymentIntegrationException("Stripe session parse error",ex);
        }
    }

    private String sendStripeRequest(String url,String method,String body){

        try{

            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization","Bearer "+secretKey);

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