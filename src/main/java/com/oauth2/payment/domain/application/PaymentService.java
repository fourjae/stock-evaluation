package com.oauth2.payment.domain.application;

import com.oauth2.dto.response.PaymentCreateResponse;
import com.oauth2.dto.response.PaymentDetailResponse;
import com.oauth2.dto.response.PaymentSummaryResponse;
import com.oauth2.payment.domain.Payment;
import com.oauth2.payment.domain.application.query.ChargePaymentCommand;
import com.oauth2.payment.domain.application.query.PaymentListView;
import com.oauth2.payment.domain.application.query.PaymentSearchCriteria;
import com.oauth2.payment.domain.port.out.PaymentGateway;
import com.oauth2.payment.domain.port.out.PaymentCommandRepositoryPort;
import com.oauth2.payment.domain.port.out.PaymentQueryRepositoryPort;
import com.oauth2.payment.domain.port.out.dto.GatewayChargeResult;
import com.oauth2.payment.domain.port.presentation.dto.PaymentDetailCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentGateway paymentGateway;
    private final PaymentCommandRepositoryPort paymentCommandRepository;
    private final PaymentQueryRepositoryPort paymentQueryRepository;

    /**
     * 결제 생성 & 실행
     */
    public PaymentCreateResponse executePayment(ChargePaymentCommand command) {
        // 1) 외부 결제 1번
        GatewayChargeResult gwRes = paymentGateway.charge(command.toGatewayChargeRequest());

        // 2) 내부 저장용 도메인 생성 + 외부 결과 반영
        Payment payment = command.toPaymentDraft();           // PENDING 등 초기 상태
        payment.applyGatewayResult(gwRes);                    // SUCCEEDED/FAILED, pspId 등 반영

        Payment saved = paymentCommandRepository.save(payment);      // 리포지토리는 도메인만 받음

        // 3) 응답 DTO
        return PaymentCreateResponse.from(saved);
    }

    /**
     * 결제 목록 조회
     */
    public Page<PaymentSummaryResponse> getPayments(PaymentSearchCriteria criteria, Pageable pageable) {

        Page<PaymentListView> payments = paymentQueryRepository.findPayments(criteria, pageable);

        // 3) 결과 변환
        return payments.map(PaymentSummaryResponse::from);
    }

    /**
     * 결제 단건 조회
     */
    public PaymentDetailResponse getPayment(PaymentDetailCriteria criteria) {

        Payment payment = paymentQueryRepository.findByPaymentKey(criteria)
                .orElseThrow(() -> new NotFoundException("결제를 찾을 수 없습니다."));

        return PaymentDetailResponse.from(payment);
    }

}
