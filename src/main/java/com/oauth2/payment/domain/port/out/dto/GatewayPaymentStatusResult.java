package com.oauth2.payment.domain.port.out.dto;

import com.oauth2.constants.payment.GatewayPaymentStatus;

import java.math.BigDecimal;
import java.time.Instant;

public record GatewayPaymentStatusResult(
        String gatewayPaymentId,
        GatewayPaymentStatus status,   // PG 관점 상태 (표준화)
        BigDecimal paidAmount,         // 결제 완료 금액 (가능하면)
        String currency,               // "KRW" 등 (선택)
        String rawStatus,              // PG 원문 상태값 (디버깅/로그용)
        String reason,                 // 실패/취소 사유
        Instant approvedAt,            // 승인 시각
        Instant canceledAt,            // 취소 시각
        Instant updatedAt              // PG가 주는 마지막 업데이트 시각
) {
    public boolean isFinalStatus() {
        return status.isFinal();
    }
}