package com.example.springstarterpayment.properties.freedom_pay;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "payment.freedompay")
public class FreedomPayProperties {

    /**
     * Merchant ID.
     */
    private String merchantId;

    /**
     * Secret key.
     */
    private String secretKey;

    /**
     * API base URL.
     */
    private String apiBase = "https://api.freedompay.kz";

    /**
     * Currency.
     */
    private String currency = "KZT";

    /**
     * Result URL (payment success).
     */
    private String successUrl;

    /**
     * Failure URL.
     */
    private String failUrl;

    /**
     * Webhook callback URL.
     */
    private String callbackUrl;

    /**
     * Enable sandbox.
     */
    private boolean sandbox = false;

    /**
     * HTTP timeout.
     */
    private int connectTimeout = 5000;

    private int readTimeout = 10000;

    // getters setters

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
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

    public String getSuccessUrl() {
        return successUrl;
    }

    public void setSuccessUrl(String successUrl) {
        this.successUrl = successUrl;
    }

    public String getFailUrl() {
        return failUrl;
    }

    public void setFailUrl(String failUrl) {
        this.failUrl = failUrl;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public boolean isSandbox() {
        return sandbox;
    }

    public void setSandbox(boolean sandbox) {
        this.sandbox = sandbox;
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
