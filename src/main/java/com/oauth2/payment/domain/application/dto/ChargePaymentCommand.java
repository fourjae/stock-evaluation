package com.oauth2.payment.domain.application.dto;

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
}
