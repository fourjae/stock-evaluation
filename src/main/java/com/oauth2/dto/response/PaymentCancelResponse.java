package com.oauth2.dto.response;

import com.oauth2.constants.payment.PaymentStatus;
import com.oauth2.payment.domain.Payment;

import java.time.OffsetDateTime;

public record PaymentCancelResponse(
    String paymentKey,
    PaymentStatus status,
    String cancelReason,
    OffsetDateTime canceledAt
) {
    public static PaymentCancelResponse from(Payment p) {
        return new PaymentCancelResponse(
                p.getPaymentKey(),
                p.getPaymentStatus(),
                p.getCancelReason(),
                p.getUpdatedAt()   // or canceledAt 필드 따로 만들기
        );
    }
}