package com.oauth2.service;

import com.oauth2.constants.payment.PaymentStatus;
import com.oauth2.dto.response.PaymentResponse;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    /**
     * 결제 생성 & 실행
     */
    public PaymentResponse executePayment() {
        return PaymentResponse.builder()
                .paymentId("1")
                .paymentStatus(PaymentStatus.SUCCEEDED)
                .build();
    }
}
