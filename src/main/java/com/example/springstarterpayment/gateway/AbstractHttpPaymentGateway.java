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
import java.util.Map;

public abstract class AbstractHttpPaymentGateway implements PaymentGateway {

    protected final HttpClient httpClient;
    protected final ObjectMapper objectMapper = new ObjectMapper();

    protected AbstractHttpPaymentGateway(int connectTimeout) {

        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis(connectTimeout))
                .build();
    }

    protected String sendRequest(
            String url,
            String method,
            String body,
            Map<String,String> headers
    ){

        try{

            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .uri(URI.create(url));

            if(headers != null){
                headers.forEach(builder::header);
            }

            if("POST".equalsIgnoreCase(method)){

                builder.header("Content-Type","application/x-www-form-urlencoded");

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
}