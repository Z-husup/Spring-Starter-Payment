package com.example.springstarterpayment.controller;

import com.example.springstarterpayment.gateway.PaymentGateway;
import com.example.springstarterpayment.properties.PaymentUiProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    /**
     * UI page with payment method selection
     */
    @GetMapping("${payment.ui.path:/payment}")
    public ResponseEntity<Resource> paymentPage() {

        ClassPathResource resource =
                new ClassPathResource("payment-ui/payment-methods.html");

        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(resource);
    }

    /**
     * Returns active payment providers
     */
    @GetMapping("${payment.ui.path:/payment}/providers")
    public List<String> providers() {

        return gateways.keySet()
                .stream()
                .map(Enum::name)
                .toList();
    }

    /**
     * Starts payment with selected provider
     */
    @GetMapping("${payment.ui.path:/payment}/{provider}")
    public ResponseEntity<Void> pay(
            @PathVariable PaymentGateway.PaymentProvider provider) {

        PaymentGateway gateway = gateways.get(provider);

        if (gateway == null) {
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

        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create(response.redirectUrl()))
                .build();
    }
}