package com.oauth2.payment.domain.application.dto;

import com.oauth2.constants.payment.PaymentStatus;
import com.oauth2.payment.domain.Payment;
import com.oauth2.payment.domain.port.out.dto.GatewayChargeRequest;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.Map;

@Builder
public record ChargePaymentCommand(
    BigDecimal amount,
    String currency,
    String paymentMethodId,
    String customerId,
    Map<String, String> metadata,
    String idemKey
) {
    // 외부 게이트웨이 호출용 port DTO
    public GatewayChargeRequest toGatewayChargeRequest() {
        return GatewayChargeRequest.builder()
                .idempotencyKey(idemKey)
                .amount(amount)
                .currency(currency)
                .paymentMethodId(paymentMethodId)
                .metadata(metadata)
                .build();
    }

    public Payment toPaymentDraft() {
        return Payment.builder()
                      .amount(amount)
                      .currency(currency)
                      .customerId(customerId)
                      .methodId(paymentMethodId)
                      .paymentStatus(PaymentStatus.PENDING)
                      .build();
    }
}
