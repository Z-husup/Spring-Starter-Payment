package com.example.springstarterpayment.gateway;

import com.example.springstarterpayment.exception.PaymentIntegrationException;
import tools.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Map;

public abstract class AbstractHttpPaymentGateway implements PaymentGateway {

    protected final HttpClient httpClient;
    protected final ObjectMapper objectMapper = new ObjectMapper();
    private final int readTimeout;

    protected AbstractHttpPaymentGateway(int connectTimeout, int readTimeout) {

        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis(connectTimeout))
                .build();
        this.readTimeout = readTimeout;
    }

    protected String sendRequest(
            String url,
            String method,
            String body,
            Map<String,String> headers
    ){

        try{

            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofMillis(readTimeout));

            if(headers != null){
                headers.forEach(builder::header);
            }

            if("POST".equalsIgnoreCase(method)){

                if (!hasHeader(headers, "Content-Type")) {
                    builder.header("Content-Type","application/x-www-form-urlencoded");
                }

                builder.POST(HttpRequest.BodyPublishers.ofString(body));

            }else{
                builder.GET();
            }

            HttpResponse<String> response =
                    httpClient.send(builder.build(),
                            HttpResponse.BodyHandlers.ofString());

            if(response.statusCode() < 200 || response.statusCode() >= 300){

                throw new PaymentIntegrationException(
                        "Payment API error " + response.statusCode() + " " + response.body());
            }

            return response.body();

        }catch(Exception ex){
            throw new PaymentIntegrationException("Payment request failed",ex);
        }
    }

    protected String encode(String value){
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    protected void validatePaymentRequest(PaymentRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Payment request must not be null");
        }
        if (request.amountCents() <= 0) {
            throw new IllegalArgumentException("Payment amount must be greater than zero");
        }
        if (isBlank(request.currency())) {
            throw new IllegalArgumentException("Payment currency must not be blank");
        }
        if (isBlank(request.successUrl()) || isBlank(request.cancelUrl())) {
            throw new IllegalArgumentException("Payment successUrl and cancelUrl must not be blank");
        }

        List<LineItem> items = request.items();
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Payment must contain at least one line item");
        }
        for (LineItem item : items) {
            if (item == null || isBlank(item.name())) {
                throw new IllegalArgumentException("Payment line item name must not be blank");
            }
            if (item.unitAmountCents() <= 0 || item.quantity() <= 0) {
                throw new IllegalArgumentException("Payment line item amount and quantity must be greater than zero");
            }
        }
    }

    private boolean hasHeader(Map<String,String> headers, String name) {
        return headers != null && headers.keySet().stream()
                .anyMatch(header -> header.equalsIgnoreCase(name));
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
