package com.example.springstarterpayment.autoconfigure.freedom_pay;

import com.example.springstarterpayment.gateway.PaymentGateway;
import com.example.springstarterpayment.gateway.freedom_pay.FreedomPayPaymentGateway;
import com.example.springstarterpayment.properties.freedom_pay.FreedomPayProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties(FreedomPayProperties.class)
@ConditionalOnProperty(prefix="payment.freedompay",name="merchant-id")
public class FreedomPayAutoConfiguration {

    @Bean
    public PaymentGateway freedomPayGateway(FreedomPayProperties properties){
        return new FreedomPayPaymentGateway(properties);
    }
}