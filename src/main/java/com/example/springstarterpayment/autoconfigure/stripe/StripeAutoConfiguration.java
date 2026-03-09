package com.example.springstarterpayment.autoconfigure.stripe;

import com.example.springstarterpayment.gateway.PaymentGateway;
import com.example.springstarterpayment.gateway.stripe.StripePaymentGateway;
import com.example.springstarterpayment.properties.stripe.StripeProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties(StripeProperties.class)
@ConditionalOnProperty(prefix="payment.stripe",name="secret-key")
public class StripeAutoConfiguration {

    @Bean
    public PaymentGateway stripeGateway(StripeProperties properties){
        return new StripePaymentGateway(properties);
    }
}