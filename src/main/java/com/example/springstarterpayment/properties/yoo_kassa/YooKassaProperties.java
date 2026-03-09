package com.example.springstarterpayment.properties.yoo_kassa;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "payment.yookassa")
public class YooKassaProperties {

    /**
     * YooKassa shop identifier.
     */
    private String shopId;

    /**
     * YooKassa secret key.
     */
    private String secretKey;

    /**
     * API base URL.
     */
    private String apiBase = "https://api.yookassa.ru/v3";

    /**
     * Default currency.
     */
    private String defaultCurrency = "rub";

    /**
     * Enable test mode.
     */
    private boolean sandbox = false;

    /**
     * HTTP connect timeout.
     */
    private int connectTimeout = 5000;

    /**
     * HTTP read timeout.
     */
    private int readTimeout = 10000;

    /**
     * Payment confirmation configuration.
     */
    private Confirmation confirmation = new Confirmation();

    public static class Confirmation {

        /**
         * Confirmation type: redirect / embedded.
         */
        private String type = "redirect";

        /**
         * Return URL.
         */
        private String returnUrl;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getReturnUrl() {
            return returnUrl;
        }

        public void setReturnUrl(String returnUrl) {
            this.returnUrl = returnUrl;
        }
    }

    // getters setters

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
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

    public String getDefaultCurrency() {
        return defaultCurrency;
    }

    public void setDefaultCurrency(String defaultCurrency) {
        this.defaultCurrency = defaultCurrency;
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

    public Confirmation getConfirmation() {
        return confirmation;
    }

    public void setConfirmation(Confirmation confirmation) {
        this.confirmation = confirmation;
    }
}