package com.example.springstarterpayment.controller;

import com.example.springstarterpayment.gateway.PaymentGateway;
import com.example.springstarterpayment.properties.PaymentProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
public class PaymentUiController {

    private final PaymentGateway gateway;
    private final PaymentProperties properties;

    public PaymentUiController(PaymentGateway gateway,
                               PaymentProperties properties) {
        this.gateway = gateway;
        this.properties = properties;
    }

    @GetMapping("${payment.ui.path:/payment}")
    public ResponseEntity<String> openPaymentPage() {

        PaymentGateway.PaymentRequest request =
                new PaymentGateway.PaymentRequest(
                        properties.getUi().getAmountCents(),
                        properties.getStripe().getDefaultCurrency(),
                        PaymentGateway.PaymentType.ONE_TIME,
                        properties.getUi().getDescription(),
                        "demo-user",
                        List.of(
                                new PaymentGateway.LineItem(
                                        properties.getUi().getDescription(),
                                        properties.getUi().getAmountCents(),
                                        1
                                )
                        ),
                        Map.of(),
                        "http://localhost:8080/payment-success",
                        "http://localhost:8080/payment-cancel"
                );

        PaymentGateway.PaymentResponse response =
                gateway.createPayment(request);

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(response.redirectUrl()))
                .build();
    }
}