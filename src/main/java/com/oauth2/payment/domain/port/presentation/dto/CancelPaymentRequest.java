package com.oauth2.payment.domain.port.presentation.dto;


import com.oauth2.dto.request.payment.PaymentCancelCommand;

/**
 * 결제 취소 요청 DTO
 * - paymentKey: 어떤 결제를 취소할 건지 (외부 노출용 ID)
 * - cancelReason: 취소 사유 (선택)
 */
public record CancelPaymentRequest(
        String paymentKey,
        String cancelReason
) {
    public PaymentCancelCommand toCommand(String userId, String paymentKey) {
        return PaymentCancelCommand.builder()
                .paymentKey(paymentKey)
                .userId(userId)
                .cancelReason(cancelReason)
                .build();
    }
}