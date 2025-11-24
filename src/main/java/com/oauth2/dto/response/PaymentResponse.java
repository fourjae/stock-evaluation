package com.oauth2.dto.response;

import com.oauth2.constants.payment.PaymentStatus;
import com.oauth2.payment.domain.Payment;
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
    public static PaymentResponse from(Payment payment) {
        return PaymentResponse.builder()
                .paymentId(payment.getId())
                .paymentStatus(payment.getPaymentStatus())
                .amount(payment.getAmount())
                .gatewayPaymentId(payment.getGatewayPaymentId())
                .paidAt(payment.getPaidAt())
                .build();
    }
}
