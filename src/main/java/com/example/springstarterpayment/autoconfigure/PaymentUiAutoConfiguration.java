package com.example.springstarterpayment.autoconfigure;

import com.example.springstarterpayment.controller.PaymentUiController;
import com.example.springstarterpayment.gateway.PaymentGateway;
import com.example.springstarterpayment.properties.PaymentProperties;
import com.example.springstarterpayment.properties.PaymentUiProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.List;

@AutoConfiguration
@EnableConfigurationProperties(PaymentUiProperties.class)
@ConditionalOnProperty(prefix="payment.ui",name="enabled",havingValue="true")
public class PaymentUiAutoConfiguration {

    @Bean
    public PaymentUiController paymentUiController(
            List<PaymentGateway> gateways,
            PaymentUiProperties properties){

        return new PaymentUiController(gateways,properties);
    }
}