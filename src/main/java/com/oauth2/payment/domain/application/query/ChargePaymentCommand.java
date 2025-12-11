package com.oauth2.payment.domain.application.query;

import com.oauth2.constants.payment.PaymentStatus;
import com.oauth2.payment.domain.Payment;
import com.oauth2.payment.domain.port.out.dto.GatewayChargeRequest;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.Map;

@Builder
public record ChargePaymentCommand(
        BigDecimal amount,
        String currency,
        String paymentMethodId,
        String customerId,
        Map<String, String> metadata,
        String idemKey
) {
    // 외부 게이트웨이 호출용 port DTO
    public GatewayChargeRequest toGatewayChargeRequest() {
        return GatewayChargeRequest.builder()
                .idempotencyKey(idemKey)
                .amount(amount)
                .currency(currency)
                .paymentMethodId(paymentMethodId)
                .metadata(metadata)
                .build();
    }

    public Payment toPaymentDraft() {
        return Payment.builder()
                .amount(amount)
                .currency(currency)
                .customerId(customerId)
                .methodId(paymentMethodId)
                .metadata(metadata)
                .paymentStatus(PaymentStatus.PENDING)
                .build();
    }

    /**
     * 재시도 시 사용: Payment -> ChargePaymentCommand
     * idemKey는 재시도에서는 보통 새로 받지 않기 때문에 null로 둔다.
     */
    public static ChargePaymentCommand from(Payment payment) {
        return ChargePaymentCommand.builder()
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .paymentMethodId(payment.getMethodId())      // ⭐ 결제수단 ID에 매핑
                .customerId(payment.getCustomerId())
                .metadata(payment.getMetadata())             // 없다면 null 또는 빈 맵
                .idemKey(null)                               // 재시도 시 멱등키는 보통 별도 관리
                .build();
    }

}
