package com.example.springstarterpayment.autoconfigure;

import com.example.springstarterpayment.controller.PaymentUiController;
import com.example.springstarterpayment.gateway.PaymentGateway;
import com.example.springstarterpayment.properties.PaymentProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "payment.ui", name = "enabled", havingValue = "true")
public class PaymentUiAutoConfiguration {

    @Bean
    public PaymentUiController paymentUiController(
            PaymentGateway gateway,
            PaymentProperties properties) {

        return new PaymentUiController(gateway, properties);
    }
}