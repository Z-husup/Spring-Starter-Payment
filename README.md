# Spring Starter Payment

Reusable Spring Boot starter for integrating multiple payment providers through a single API.

`Spring Starter Payment` abstracts provider-specific payment logic behind a common `PaymentGateway` contract and uses Spring Boot auto-configuration to register only the providers configured by the host application.

## Features

- Unified payment API for different providers
- Spring Boot auto-configuration
- Conditional provider registration by configuration properties
- Built-in support for Stripe, PayPal, and CloudPayments
- Optional payment method selection UI
- Configuration validation with Jakarta Bean Validation
- Configurable connect and read timeouts
- Override-friendly provider beans
- Extensible provider architecture

## Supported Providers

| Provider | Create Payment | Payment Status | Notes |
|---|---:|---:|---|
| Stripe | Yes | Yes | Uses Checkout Sessions |
| PayPal | Yes | Yes | Uses Orders API |
| CloudPayments | Yes | Basic | Redirect/widget flow |

## Requirements

- Java 21+
- Spring Boot 4.x
- Gradle

## Installation

Add the starter to your Spring Boot application:

```groovy
dependencies {
    implementation "com.example:spring-starter-payment:0.0.1-SNAPSHOT"
}
```

For local development, publish the library to your local Maven repository or include it as a Gradle composite build.

## Quick Start

Configure at least one provider:

```properties
payment.stripe.secret-key=sk_test_xxx
payment.stripe.default-currency=usd
payment.stripe.connect-timeout=5000
payment.stripe.read-timeout=10000
```

Inject and use `PaymentGateway`:

```java
@RestController
@RequestMapping("/checkout")
public class CheckoutController {

    private final PaymentGateway paymentGateway;

    public CheckoutController(PaymentGateway paymentGateway) {
        this.paymentGateway = paymentGateway;
    }

    @PostMapping
    public ResponseEntity<Void> createPayment(@RequestBody CheckoutRequest checkoutRequest) {
        PaymentGateway.PaymentRequest request = new PaymentGateway.PaymentRequest(
                checkoutRequest.amountCents(),
                checkoutRequest.currency(),
                PaymentGateway.PaymentType.ONE_TIME,
                checkoutRequest.description(),
                checkoutRequest.customerReference(),
                List.of(new PaymentGateway.LineItem(
                        checkoutRequest.description(),
                        checkoutRequest.amountCents(),
                        1
                )),
                Map.of("orderId", checkoutRequest.orderId()),
                "https://example.com/payment/success",
                "https://example.com/payment/cancel"
        );

        PaymentGateway.PaymentResponse response = paymentGateway.createPayment(request);

        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create(response.redirectUrl()))
                .build();
    }
}
```

If multiple providers are configured, inject `List<PaymentGateway>` or build a provider registry in your application.

## Configuration

### Stripe

Prefix: `payment.stripe`

| Property | Required | Default | Description |
|---|---:|---|---|
| `secret-key` | Yes | - | Stripe secret API key |
| `publishable-key` | No | - | Stripe publishable key |
| `webhook-secret` | No | - | Stripe webhook signing secret |
| `api-base` | No | `https://api.stripe.com` | Stripe API base URL |
| `api-version` | No | - | Optional Stripe API version |
| `default-currency` | No | `usd` | Default payment currency |
| `connect-timeout` | No | `5000` | HTTP connect timeout in milliseconds |
| `read-timeout` | No | `10000` | HTTP request timeout in milliseconds |

Example:

```properties
payment.stripe.secret-key=sk_test_xxx
payment.stripe.default-currency=usd
payment.stripe.connect-timeout=5000
payment.stripe.read-timeout=10000
```

### PayPal

Prefix: `payment.paypal`

| Property | Required | Default | Description |
|---|---:|---|---|
| `client-id` | Yes | - | PayPal client ID |
| `client-secret` | Yes | - | PayPal client secret |
| `api-base` | No | `https://api-m.sandbox.paypal.com` | PayPal API base URL |
| `currency` | No | `USD` | Payment currency |
| `connect-timeout` | No | `5000` | HTTP connect timeout in milliseconds |
| `read-timeout` | No | `10000` | HTTP request timeout in milliseconds |

Example:

```properties
payment.paypal.client-id=client_id
payment.paypal.client-secret=client_secret
payment.paypal.currency=USD
```

### CloudPayments

Prefix: `payment.cloudpayments`

| Property | Required | Default | Description |
|---|---:|---|---|
| `public-id` | Yes | - | CloudPayments public ID |
| `api-secret` | No | - | CloudPayments API secret |
| `api-base` | No | `https://api.cloudpayments.ru` | CloudPayments API base URL |
| `currency` | No | `RUB` | Payment currency |
| `connect-timeout` | No | `5000` | HTTP connect timeout in milliseconds |
| `read-timeout` | No | `10000` | HTTP request timeout in milliseconds |

Example:

```properties
payment.cloudpayments.public-id=pk_xxx
payment.cloudpayments.currency=RUB
```

## Optional Payment UI

The starter can expose a simple payment method selection page.

```properties
payment.ui.enabled=true
payment.ui.path=/payment
payment.ui.amount-cents=2000
payment.ui.currency=usd
payment.ui.description=Demo payment
payment.ui.success-url=https://example.com/payment/success
payment.ui.cancel-url=https://example.com/payment/cancel
```

When enabled, the following endpoints are available:

| Endpoint | Description |
|---|---|
| `GET {payment.ui.path}` | Payment method selection page |
| `GET {payment.ui.path}/providers` | Active configured providers |
| `GET {payment.ui.path}/{provider}` | Starts a payment with the selected provider |

The UI path is injected into the HTML at runtime, so custom paths such as `/checkout/payment` are supported.

## Auto-Configuration

Provider beans are registered only when their trigger property is present:

| Provider | Trigger Property | Bean Name |
|---|---|---|
| Stripe | `payment.stripe.secret-key` | `stripeGateway` |
| PayPal | `payment.paypal.client-id` | `paypalGateway` |
| CloudPayments | `payment.cloudpayments.public-id` | `cloudPaymentsGateway` |

Each auto-configured bean uses `@ConditionalOnMissingBean(name = "...")`, so applications can replace a provider implementation by defining a bean with the same name.

## Public API

The main interface is `PaymentGateway`:

```java
public interface PaymentGateway {

    PaymentProvider provider();

    PaymentResponse createPayment(PaymentRequest request);

    PaymentStatusResponse getPaymentStatus(String paymentId);
}
```

Supported statuses:

```java
CREATED, PENDING, PAID, FAILED, CANCELED
```

Supported payment types:

```java
ONE_TIME, SUBSCRIPTION
```

## Extending With a New Provider

To add another provider:

1. Create a new `@ConfigurationProperties` class.
2. Add validation constraints for required settings.
3. Implement `PaymentGateway`, preferably by extending `AbstractHttpPaymentGateway`.
4. Create an `@AutoConfiguration` class.
5. Register it in `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`.
6. Add the provider to `PaymentGateway.PaymentProvider`.
7. Add tests and document the configuration.

## Project Structure

```text
src/main/java/com/example/springstarterpayment
├── autoconfigure
│   ├── cloudpayments
│   ├── paypal
│   └── stripe
├── controller
├── exception
├── gateway
│   ├── cloudpayments
│   ├── paypal
│   └── stripe
└── properties
    ├── cloudpayments
    ├── paypal
    └── stripe
```

## Build and Test

Compile the project:

```bash
./gradlew clean compileJava
```

Run tests:

```bash
./gradlew test
```

The test suite covers:

- provider auto-configuration
- configuration validation
- provider bean override behavior
- payment UI path replacement

## Current Limitations

This project provides the foundation for a reusable payment starter, but it is not a complete payment platform yet.

Current limitations:

- no webhook processing API
- no webhook signature verification
- no persistence layer for payment history
- no idempotency key support
- no refund or capture API
- no circuit breaker or retry execution
- limited CloudPayments status handling

For production use, add provider webhook handling, idempotency, structured logging, metrics, audit storage, and stronger provider-specific error mapping.
