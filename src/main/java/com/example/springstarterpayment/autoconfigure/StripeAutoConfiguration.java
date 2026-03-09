package com.example.springstarterpayment.autoconfigure;

import com.example.springstarterpayment.gateway.PaymentGateway;
import com.example.springstarterpayment.gateway.StripePaymentGateway;
import com.example.springstarterpayment.properties.PaymentProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

@AutoConfiguration
@EnableConfigurationProperties(PaymentProperties.class)
public class StripeAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public PaymentGateway paymentGateway(PaymentProperties properties) {

        return new StripePaymentGateway(
                properties.getSecretKey()
        );
    }
}