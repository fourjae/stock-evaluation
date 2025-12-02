package com.oauth2.payment.domain.port.presentation.dto;


import com.oauth2.constants.payment.PaymentStatus;
import com.oauth2.payment.domain.application.query.PaymentSearchCriteria;
import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.Objects;


public record PaymentDetailCriteria(
        String paymentKey,
        String orderId,
        String userId
) {
    public static PaymentDetailCriteria byPaymentKey(String paymentKey, String userId) {
        return new PaymentDetailCriteria(paymentKey, null, userId);
    }

    public static PaymentDetailCriteria byOrderId(String orderId, String userId) {
        return new PaymentDetailCriteria(null, orderId, userId);
    }
}
