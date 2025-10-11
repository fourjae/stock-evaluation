package com.oauth2.payment.domain.application;

import com.oauth2.constants.payment.PaymentStatus;
import com.oauth2.dto.response.PaymentResponse;
import com.oauth2.payment.domain.port.out.PaymentGateway;
import com.oauth2.payment.domain.port.out.dto.GatewayChargeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentGateway paymentGateway;

    /**
     * 결제 생성 & 실행
     */
    public PaymentResponse executePayment() {
        GatewayChargeRequest req = new GatewayChargeRequest();
        paymentGateway.charge(req);
        return PaymentResponse.builder()
                .paymentId("1")
                .paymentStatus(PaymentStatus.SUCCEEDED)
                .build();
    }
}
