package com.example.springstarterpayment.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "payment")
public class PaymentProperties {

    private Stripe stripe = new Stripe();
    private Defaults defaults = new Defaults();

    public Stripe getStripe() {
        return stripe;
    }

    public void setStripe(Stripe stripe) {
        this.stripe = stripe;
    }

    public Defaults getDefaults() {
        return defaults;
    }

    public void setDefaults(Defaults defaults) {
        this.defaults = defaults;
    }

    public static class Stripe {

        /**
         * Stripe secret API key.
         */
        private String secretKey;

        /**
         * Stripe publishable key (for frontend integrations).
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
         * Stripe API version override.
         */
        private String apiVersion;

        /**
         * HTTP connection timeout in milliseconds.
         */
        private int connectTimeout = 5000;

        /**
         * HTTP read timeout in milliseconds.
         */
        private int readTimeout = 10000;

        /**
         * Default currency fallback.
         */
        private String defaultCurrency = "usd";

        private Retry retry = new Retry();
        private Connect connect = new Connect();

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

        public String getDefaultCurrency() {
            return defaultCurrency;
        }

        public void setDefaultCurrency(String defaultCurrency) {
            this.defaultCurrency = defaultCurrency;
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

        public static class Retry {

            /**
             * Maximum number of retry attempts for Stripe requests.
             */
            private int maxAttempts = 3;

            /**
             * Backoff time between retries in milliseconds.
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
             * Enables Stripe Connect platform mode.
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
    }

    public static class Defaults {

        /**
         * Default payment type used when request does not specify one.
         */
        private PaymentType paymentType = PaymentType.ONE_TIME;

        /**
         * Default expiration time for checkout sessions (seconds).
         */
        private long expirationSeconds = 1800;

        /**
         * Default subscription configuration.
         */
        private Subscription subscription = new Subscription();

        public PaymentType getPaymentType() {
            return paymentType;
        }

        public void setPaymentType(PaymentType paymentType) {
            this.paymentType = paymentType;
        }

        public long getExpirationSeconds() {
            return expirationSeconds;
        }

        public void setExpirationSeconds(long expirationSeconds) {
            this.expirationSeconds = expirationSeconds;
        }

        public Subscription getSubscription() {
            return subscription;
        }

        public void setSubscription(Subscription subscription) {
            this.subscription = subscription;
        }

        public static class Subscription {

            /**
             * Default trial period in days.
             */
            private int trialDays = 0;

            public int getTrialDays() {
                return trialDays;
            }

            public void setTrialDays(int trialDays) {
                this.trialDays = trialDays;
            }
        }
    }

    public enum PaymentType {
        ONE_TIME,
        SUBSCRIPTION
    }

    public static class Ui {

        /**
         * Enables a default payment test page.
         */
        private boolean enabled = false;

        /**
         * Path where the payment page is exposed.
         */
        private String path = "/payment";

        /**
         * Default payment amount (cents).
         */
        private long amountCents = 1000;

        /**
         * Default description.
         */
        private String description = "Test Payment";

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public long getAmountCents() {
            return amountCents;
        }

        public void setAmountCents(long amountCents) {
            this.amountCents = amountCents;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    private Ui ui = new Ui();

    public Ui getUi() {
        return ui;
    }

    public void setUi(Ui ui) {
        this.ui = ui;
    }
}