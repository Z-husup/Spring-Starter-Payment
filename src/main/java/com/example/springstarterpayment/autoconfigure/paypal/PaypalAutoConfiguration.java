package com.example.springstarterpayment.autoconfigure.paypal;

import com.example.springstarterpayment.gateway.PaymentGateway;
import com.example.springstarterpayment.gateway.paypal.PaypalPaymentGateway;

import com.example.springstarterpayment.properties.paypal.PaypalProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties(PaypalProperties.class)
@ConditionalOnProperty(prefix="payment.paypal",name="client-id")
public class PaypalAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(name = "paypalGateway")
    public PaymentGateway paypalGateway(PaypalProperties properties){
        return new PaypalPaymentGateway(properties);
    }
}
