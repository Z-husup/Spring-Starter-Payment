package com.example.springstarterpayment.gateway.cloudpayments;

import com.example.springstarterpayment.gateway.AbstractHttpPaymentGateway;
import com.example.springstarterpayment.gateway.PaymentGateway;
import com.example.springstarterpayment.properties.cloudpayments.CloudPaymentsProperties;

import java.util.UUID;

public class CloudPaymentsPaymentGateway extends AbstractHttpPaymentGateway {

    @Override
    public PaymentProvider provider() {
        return PaymentProvider.CLOUDPAYMENTS;
    }

    private final CloudPaymentsProperties properties;

    public CloudPaymentsPaymentGateway(CloudPaymentsProperties properties) {
        super(properties.getConnectTimeout());
        this.properties = properties;
    }

    @Override
    public PaymentResponse createPayment(PaymentRequest request) {

        String redirect =
                "https://widget.cloudpayments.ru/" +
                        "?publicId="+properties.getPublicId() +
                        "&amount="+request.amountCents()/100.0 +
                        "&currency="+properties.getCurrency() +
                        "&invoiceId="+ UUID.randomUUID();

        return new PaymentResponse(
                UUID.randomUUID().toString(),
                null,
                redirect
        );
    }

    @Override
    public PaymentStatusResponse getPaymentStatus(String paymentId) {

        return new PaymentStatusResponse(
                paymentId,
                PaymentStatus.PENDING
        );
    }
}