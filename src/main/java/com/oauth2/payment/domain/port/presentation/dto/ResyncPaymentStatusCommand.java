package com.oauth2.payment.domain.port.presentation.dto;

public record ResyncPaymentStatusCommand(
    String paymentKey,
    String userId
) {
    public static ResyncPaymentStatusCommand of(String paymentKey, String userId) {
        return new ResyncPaymentStatusCommand(paymentKey, userId);
    }
}

