package com.example.springstarterpayment.autoconfigure.yoo_kassa;

import com.example.springstarterpayment.gateway.PaymentGateway;
import com.example.springstarterpayment.gateway.yoo_kassa.YooKassaPaymentGateway;
import com.example.springstarterpayment.properties.yoo_kassa.YooKassaProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties(YooKassaProperties.class)
@ConditionalOnProperty(prefix="payment.yookassa",name="shop-id")
public class YooKassaAutoConfiguration {

    @Bean
    public PaymentGateway yooKassaGateway(YooKassaProperties properties){
        return new YooKassaPaymentGateway(properties);
    }
}