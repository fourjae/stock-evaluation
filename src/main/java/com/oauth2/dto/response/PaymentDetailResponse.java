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

    public static PaymentDetailResponse from(Payment p) {
        return new PaymentDetailResponse(
                p.getPaymentKey(),
                p.getGatewayPaymentId(),
                p.getAmount(),
                p.getCurrency(),
                p.getPaymentStatus(),
                p.getCustomerId(),
                p.getMethodId(),
                p.getFailureCode(),
                p.getFailureMessage(),
                p.getPaidAt(),
                p.getCreatedAt(),
                p.getCreatedBy(),
                p.getUpdatedAt(),
                p.getUpdatedBy()
        );
    }

}

