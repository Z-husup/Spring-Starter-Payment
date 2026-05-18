package com.example.springstarterpayment.properties.paypal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "payment.paypal")
public class PaypalProperties {

     /**
     * PayPal client id
     */
    @NotBlank
    private String clientId;

     /**
     * PayPal client secret
     */
    @NotBlank
    private String clientSecret;

     /**
     * PayPal API base
     */
    @NotBlank
    private String apiBase = "https://api-m.sandbox.paypal.com";

     /**
     * currency
     */
    @NotBlank
    private String currency = "USD";

    @Min(1)
    private int connectTimeout = 5000;

    @Min(1)
    private int readTimeout = 10000;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getApiBase() {
        return apiBase;
    }

    public void setApiBase(String apiBase) {
        this.apiBase = apiBase;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }
}
