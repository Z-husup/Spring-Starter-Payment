package com.example.springstarterpayment.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "payment")
public class PaymentProperties {

    /**
     * Active payment provider.
     */
    private PaymentProvider provider = PaymentProvider.STRIPE;

    public PaymentProvider getProvider() {
        return provider;
    }

    public void setProvider(PaymentProvider provider) {
        this.provider = provider;
    }
}