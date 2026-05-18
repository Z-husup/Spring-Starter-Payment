package com.example.springstarterpayment.properties.stripe;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "payment.stripe")
public class StripeProperties {

     /**
     * Stripe secret API key.
     */
    @NotBlank
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
    @NotBlank
    private String apiBase = "https://api.stripe.com";

    /**
     * Optional Stripe API version.
     */
    private String apiVersion;

     /**
     * Default currency.
     */
    @NotBlank
    private String defaultCurrency = "usd";

     /**
     * HTTP connect timeout (ms).
     */
    @Min(1)
    private int connectTimeout = 5000;

     /**
     * HTTP read timeout (ms).
     */
    @Min(1)
    private int readTimeout = 10000;

    @Valid
    private Retry retry = new Retry();

    @Valid
    private Connect connect = new Connect();

    public static class Retry {

         /**
         * Max retry attempts for Stripe requests.
         */
        @Min(0)
        private int maxAttempts = 3;

         /**
         * Backoff time in milliseconds.
         */
        @Min(0)
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
