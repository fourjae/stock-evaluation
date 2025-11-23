package com.oauth2.payment.domain.port.out.dto;

import com.oauth2.constants.payment.PaymentStatus;
import lombok.Builder;

import java.util.Map;

@Builder
public record GatewayChargeResult(
    boolean succeeded,
    String gatewayPaymentId,
    PaymentStatus status, // e.g., "SUCCEEDED", "FAILED"
    String failureCode, // e.g., psp code
    String failureMessage,
    Map<String, Object> raw // for debugging
) {}
