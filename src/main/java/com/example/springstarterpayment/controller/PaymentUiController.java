package com.example.springstarterpayment.controller;

import com.example.springstarterpayment.gateway.PaymentGateway;
import com.example.springstarterpayment.properties.PaymentProperties;
import com.example.springstarterpayment.properties.PaymentUiProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
public class PaymentUiController {

    private final Map<PaymentGateway.PaymentProvider, PaymentGateway> gateways;
    private final PaymentUiProperties properties;

    public PaymentUiController(
            List<PaymentGateway> gatewayList,
            PaymentUiProperties properties) {

        this.properties = properties;

        this.gateways = gatewayList.stream()
                .collect(Collectors.toMap(
                        PaymentGateway::provider,
                        Function.identity()
                ));
    }

    @GetMapping("${payment.ui.path:/payment}")
    public List<String> getAvailableProviders() {

        return gateways.keySet()
                .stream()
                .map(Enum::name)
                .toList();
    }

    @PostMapping("${payment.ui.path:/payment}/{provider}")
    public ResponseEntity<Void> pay(
            @PathVariable PaymentGateway.PaymentProvider provider) {

        PaymentGateway gateway = gateways.get(provider);

        if(gateway == null){
            return ResponseEntity.notFound().build();
        }

        PaymentGateway.PaymentRequest request =
                new PaymentGateway.PaymentRequest(
                        properties.getAmountCents(),
                        properties.getCurrency(),
                        PaymentGateway.PaymentType.ONE_TIME,
                        properties.getDescription(),
                        "demo-user",
                        List.of(new PaymentGateway.LineItem(
                                properties.getDescription(),
                                properties.getAmountCents(),
                                1
                        )),
                        Map.of(),
                        properties.getSuccessUrl(),
                        properties.getCancelUrl()
                );

        PaymentGateway.PaymentResponse response =
                gateway.createPayment(request);

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(response.redirectUrl()))
                .build();
    }
}