package com.oauth2.dto.response;

import com.oauth2.constants.payment.PaymentStatus;
import com.oauth2.payment.domain.Payment;
import com.oauth2.payment.domain.application.query.PaymentListView;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.*;

@Builder
public record PaymentSummaryResponse(
    String paymentKey,
    String customerId,
    BigDecimal amount,
    PaymentStatus paymentStatus,
    OffsetDateTime paidAt,
    OffsetDateTime createdAt
) {
    public static PaymentSummaryResponse from(PaymentListView view) {
        return new PaymentSummaryResponse(
                view.getPaymentKey(),
                view.getCustomerId(),
                view.getAmount(),
                view.getPaymentStatus(),
                view.getPaidAt(),
                view.getCreatedAt()
        );
    }
}
