package com.oauth2.payment.domain.port.out.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.Map;

@Builder
public record GatewayChargeRequest(
        String idempotencyKey,
        BigDecimal amount,
        String currency,
        String paymentMethodId,
        Map<String, String> metadata
) {}
