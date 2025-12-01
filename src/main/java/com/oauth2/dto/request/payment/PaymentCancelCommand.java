package com.oauth2.dto.request.payment;

import lombok.Builder;

@Builder
public record PaymentCancelCommand(
    String paymentKey,
    String userId,
    String cancelReason
) {
}
