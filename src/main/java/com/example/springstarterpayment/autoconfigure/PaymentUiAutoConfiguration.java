package com.example.springstarterpayment.autoconfigure;

import com.example.springstarterpayment.controller.PaymentUiController;
import com.example.springstarterpayment.gateway.PaymentGateway;
import com.example.springstarterpayment.properties.PaymentProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ConditionalOnProperty(prefix = "payment.ui", name = "enabled", havingValue = "true")
public class PaymentUiAutoConfiguration {

    @Bean
    public PaymentUiController paymentUiController(
            PaymentGateway gateway,
            PaymentProperties properties) {

        return new PaymentUiController(gateway, properties);
    }
}