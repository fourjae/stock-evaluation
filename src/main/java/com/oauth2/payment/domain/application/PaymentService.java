package com.oauth2.payment.domain.application;

import com.oauth2.dto.response.PaymentResponse;
import com.oauth2.payment.domain.Payment;
import com.oauth2.payment.domain.application.query.ChargePaymentCommand;
import com.oauth2.payment.domain.port.out.PaymentGateway;
import com.oauth2.payment.domain.port.out.PaymentRepositoryPort;
import com.oauth2.payment.domain.port.out.dto.GatewayChargeResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentGateway paymentGateway;
    private final PaymentRepositoryPort paymentRepository;

    /**
     * 결제 생성 & 실행
     */
    public PaymentResponse executePayment(ChargePaymentCommand command) {
        // 1) 외부 결제 1번
        GatewayChargeResult gwRes = paymentGateway.charge(command.toGatewayChargeRequest());

        // 2) 내부 저장용 도메인 생성 + 외부 결과 반영
        Payment payment = command.toPaymentDraft();           // PENDING 등 초기 상태
        payment.applyGatewayResult(gwRes);                    // SUCCEEDED/FAILED, pspId 등 반영

        Payment saved = paymentRepository.save(payment);      // 리포지토리는 도메인만 받음

        // 3) 응답 DTO
        return PaymentResponse.from(saved);
    }
}
