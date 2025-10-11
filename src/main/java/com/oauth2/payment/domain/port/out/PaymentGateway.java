package com.oauth2.payment.domain.port.out;

import com.oauth2.payment.domain.port.out.dto.GatewayChargeRequest;

public interface PaymentGateway {
    // GatewayChargeResult
    void charge(GatewayChargeRequest req); // 즉시 결제 (auth+capture)
//    // GatewayAuthResult
//    void authorize(GatewayAuthRequest req); // 선승인
//    // GatewayCaptureResult
//    void capture(GatewayCaptureRequest req); // 매입
//    // GatewayVoidResult
//    void voidAuth(GatewayVoidRequest req); // 승인취소
//    // GatewayRefundResult
//    void refund(GatewayRefundRequest req); // 환불
}
