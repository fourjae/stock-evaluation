package com.oauth2.dto.response;

import com.oauth2.constants.payment.PaymentStatus;
import com.oauth2.payment.domain.Payment;
import com.oauth2.payment.domain.application.query.PaymentListView;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.*;

public record PaymentDetailResponse(
        String paymentKey,
        String gatewayPaymentId,
        BigDecimal amount,
        String currency,
        PaymentStatus paymentStatus,
        String customerId,
        String methodId,
        String failureCode,
        String failureMessage,
        OffsetDateTime paidAt,
        OffsetDateTime createdAt,
        String createdBy,
        OffsetDateTime updatedAt,
        String updatedBy
) {

    public static PaymentDetailResponse from(Payment payment) {
        return new PaymentDetailResponse(
                payment.getPaymentKey(),
                payment.getGatewayPaymentId(),
                payment.getAmount(),
                payment.getCurrency(),
                payment.getPaymentStatus(),
                payment.getCustomerId(),
                payment.getMethodId(),
                payment.getFailureCode(),
                payment.getFailureMessage(),
                payment.getPaidAt(),
                payment.getCreatedAt(),
                payment.getCreatedBy(),
                payment.getUpdatedAt(),
                payment.getUpdatedBy()
        );
    }
}

