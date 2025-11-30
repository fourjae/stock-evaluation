package com.oauth2.payment.domain.port.presentation.dto;


import com.oauth2.constants.payment.PaymentStatus;
import com.oauth2.payment.domain.application.query.PaymentSearchCriteria;
import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.Objects;

@Builder
public record PaymentDetailCriteria(
        String paymentKey,
        String userId
) {
    public PaymentDetailCriteria toCriteria(String paymentKey, String userId) {
        return PaymentDetailCriteria.builder()
                        .paymentKey(paymentKey)
                        .userId(userId)
                        .build();
    }
}
