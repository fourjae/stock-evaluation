package com.oauth2.payment.domain.port.presentation.dto;

// application.payment.command 패키지 쯤
public record RetryPaymentCommand(
        String paymentKey,
        String userId
) {
    public static RetryPaymentCommand of(String paymentKey, String userId) {
        return new RetryPaymentCommand(paymentKey, userId);
    }
}

