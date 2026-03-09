package com.example.springstarterpayment.properties.stripe;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "payment.stripe")
public class StripeProperties {

    /**
     * Stripe secret API key.
     */
    private String secretKey;

    /**
     * Stripe publishable key (frontend).
     */
    private String publishableKey;

    /**
     * Stripe webhook signing secret.
     */
    private String webhookSecret;

    /**
     * Stripe API base URL.
     */
    private String apiBase = "https://api.stripe.com";

    /**
     * Optional Stripe API version.
     */
    private String apiVersion;

    /**
     * Default currency.
     */
    private String defaultCurrency = "usd";

    /**
     * HTTP connect timeout (ms).
     */
    private int connectTimeout = 5000;

    /**
     * HTTP read timeout (ms).
     */
    private int readTimeout = 10000;

    private Retry retry = new Retry();

    private Connect connect = new Connect();

    public static class Retry {

        /**
         * Max retry attempts for Stripe requests.
         */
        private int maxAttempts = 3;

        /**
         * Backoff time in milliseconds.
         */
        private long backoffMillis = 500;

        public int getMaxAttempts() {
            return maxAttempts;
        }

        public void setMaxAttempts(int maxAttempts) {
            this.maxAttempts = maxAttempts;
        }

        public long getBackoffMillis() {
            return backoffMillis;
        }

        public void setBackoffMillis(long backoffMillis) {
            this.backoffMillis = backoffMillis;
        }
    }

    public static class Connect {

        /**
         * Enable Stripe Connect.
         */
        private boolean enabled = false;

        /**
         * Platform account ID.
         */
        private String platformAccountId;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getPlatformAccountId() {
            return platformAccountId;
        }

        public void setPlatformAccountId(String platformAccountId) {
            this.platformAccountId = platformAccountId;
        }
    }

    // getters setters

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getPublishableKey() {
        return publishableKey;
    }

    public void setPublishableKey(String publishableKey) {
        this.publishableKey = publishableKey;
    }

    public String getWebhookSecret() {
        return webhookSecret;
    }

    public void setWebhookSecret(String webhookSecret) {
        this.webhookSecret = webhookSecret;
    }

    public String getApiBase() {
        return apiBase;
    }

    public void setApiBase(String apiBase) {
        this.apiBase = apiBase;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public String getDefaultCurrency() {
        return defaultCurrency;
    }

    public void setDefaultCurrency(String defaultCurrency) {
        this.defaultCurrency = defaultCurrency;
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

    public Retry getRetry() {
        return retry;
    }

    public void setRetry(Retry retry) {
        this.retry = retry;
    }

    public Connect getConnect() {
        return connect;
    }

    public void setConnect(Connect connect) {
        this.connect = connect;
    }
}