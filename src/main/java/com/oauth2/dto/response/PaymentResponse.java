package com.oauth2.dto.response;

import com.oauth2.constants.payment.PaymentStatus;
import com.oauth2.payment.domain.Payment;
import com.oauth2.payment.domain.application.query.PaymentListView;
import lombok.Builder;

import java.util.UUID;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Builder
public record PaymentResponse(
        UUID paymentId,
        PaymentStatus paymentStatus,
        BigDecimal amount,
        String gatewayPaymentId,
        OffsetDateTime paidAt
)
{
    public static PaymentResponse from(PaymentListView view) {
        return PaymentResponse.builder()
                .paymentId(view.getId())
                .paymentStatus(view.getPaymentStatus())
                .amount(view.getAmount())
                .gatewayPaymentId(view.getGatewayPaymentId())
                .paidAt(view.getPaidAt())
                .build();
    }
}
