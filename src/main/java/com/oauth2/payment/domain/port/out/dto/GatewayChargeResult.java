package com.oauth2.payment.domain.port.out.dto;

import lombok.Builder;

import java.util.Map;

@Builder
public record GatewayChargeResult(
    boolean succeeded,
    String gatewayPaymentId,
    String status, // e.g., "SUCCEEDED", "FAILED"
    String failureCode, // e.g., psp code
    String failureMessage,
    Map<String, Object> raw // for debugging
) {}
