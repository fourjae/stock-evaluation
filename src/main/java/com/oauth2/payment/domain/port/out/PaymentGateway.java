package com.oauth2.payment.domain.port.out;

import com.oauth2.payment.domain.port.out.dto.GatewayChargeRequest;
import com.oauth2.payment.domain.port.out.dto.GatewayChargeResult;
import com.oauth2.payment.domain.port.out.dto.GatewayPaymentStatusResult;

public interface PaymentGateway {
    // GatewayChargeResult
    GatewayChargeResult charge(GatewayChargeRequest req); // 즉시 결제 (auth+capture)

    GatewayPaymentStatusResult inquire(String gatewayPaymentId);
//    // GatewayAuthResult
//    void authorize(GatewayAuthRequest req); // 선승인
//    // GatewayCaptureResult
//    void capture(GatewayCaptureRequest req); // 매입
//    // GatewayVoidResult
//    void voidAuth(GatewayVoidRequest req); // 승인취소
//    // GatewayRefundResult
//    void refund(GatewayRefundRequest req); // 환불
}
