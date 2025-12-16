package com.oauth2.constants.payment;

public enum GatewayPaymentStatus {
    READY,          // 결제 준비/생성됨
    IN_PROGRESS,    // 인증/승인 진행 중
    APPROVED,       // 승인(결제완료)
    FAILED,         // 실패
    CANCELED,       // 취소
    EXPIRED,        // 만료
    UNKNOWN;        // 매핑 불가

    public boolean isFinal() {
        return this == APPROVED || this == FAILED || this == CANCELED || this == EXPIRED;
    }
}
