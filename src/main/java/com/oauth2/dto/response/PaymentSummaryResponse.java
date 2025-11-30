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
    String id,
    String customerId,
    BigDecimal amount,
    PaymentStatus paymentStatus,
    OffsetDateTime paidAt,
    OffsetDateTime createdAt
)
{
    public static PaymentSummaryResponse from(PaymentListView view) {
        return PaymentSummaryResponse.builder()
                .id(view.getId())
                .customerId(view.getCustomerId())
                .amount(view.getAmount())
                .paymentStatus(view.getPaymentStatus())
                .paidAt(view.getPaidAt())
                .createdAt(view.getCreatedAt())
                .build();
    }
}
