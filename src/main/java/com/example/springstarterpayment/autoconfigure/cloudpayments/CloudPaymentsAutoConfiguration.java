package com.example.springstarterpayment.autoconfigure.cloudpayments;

import com.example.springstarterpayment.gateway.PaymentGateway;

import com.example.springstarterpayment.gateway.cloudpayments.CloudPaymentsPaymentGateway;
import com.example.springstarterpayment.properties.cloudpayments.CloudPaymentsProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties(CloudPaymentsProperties.class)
@ConditionalOnProperty(prefix="payment.cloudpayments",name="public-id")
public class CloudPaymentsAutoConfiguration {

    @Bean
    public PaymentGateway cloudPaymentsGateway(
            CloudPaymentsProperties properties){

        return new CloudPaymentsPaymentGateway(properties);
    }
}