package com.oauth2.dto.response;

import com.oauth2.constants.payment.PaymentStatus;
import com.oauth2.payment.domain.Payment;
import lombok.Builder;

import java.util.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Builder
public record PaymentCreateResponse(
        PaymentStatus paymentStatus,
        BigDecimal amount,
        String paymentKey,
        OffsetDateTime paidAt,
        OffsetDateTime createdAt
) {
    public static PaymentCreateResponse from(Payment p) {
        return new PaymentCreateResponse(
                p.getPaymentStatus(),
                p.getAmount(),
                p.getPaymentKey(),
                p.getPaidAt(),
                p.getCreatedAt()
        );
    }
}
