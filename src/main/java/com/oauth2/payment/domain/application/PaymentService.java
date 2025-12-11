package com.oauth2.payment.domain.application;

import com.oauth2.common.exception.NotFoundException;
import com.oauth2.dto.request.payment.PaymentCancelCommand;
import com.oauth2.dto.response.PaymentCancelResponse;
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
import com.oauth2.payment.domain.port.presentation.dto.RetryPaymentCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        // 1) 결제 조회 (paymentKey 기준)
        Payment payment = paymentQueryRepository.findByCriteria(criteria)
                .orElseThrow(() -> new NotFoundException("결제를 찾을 수 없습니다."));

        return PaymentDetailResponse.from(payment);
    }

    /**
     * 결제 취소
     */
    public PaymentCancelResponse cancelPayment(PaymentCancelCommand command) {
        // 1) 결제 조회 (paymentKey 기준)
        Payment payment = paymentQueryRepository.findByPaymentKeyAndCustomerId(
                command.paymentKey(), command.userId()
        ).orElseThrow(() -> new NotFoundException("결제를 찾을 수 없습니다."));


        // 4) 취소 처리 (도메인 메서드에 위임)
        payment.cancel(command.userId(), command.cancelReason());

        // 5) 저장
        Payment saved = paymentCommandRepository.save(payment);

        return PaymentCancelResponse.from(saved);
    }

    // application.payment.PaymentService

    @Transactional
    public PaymentCreateResponse retryPayment(RetryPaymentCommand command) {

        // 1) 결제 조회 (사용자 소유 검증까지 포함)
        Payment payment = paymentQueryRepository.findByCriteria(
                        PaymentDetailCriteria.byPaymentKey(command.paymentKey(), command.userId())
                )
                .orElseThrow(() -> new NotFoundException("결제를 찾을 수 없습니다."));

        // 2) 재시도 가능 상태인지 도메인 규칙 체크
        payment.validateRetryable();

        // ✅ 도메인 → 애플리케이션 커맨드 변환
        ChargePaymentCommand chargeCommand = ChargePaymentCommand.from(payment);

        // 3) 외부 PG 재호출
        GatewayChargeResult gwRes = paymentGateway.charge(chargeCommand.toGatewayChargeRequest());

        // 4) 외부 결과를 도메인에 반영 (상태/승인번호 등)
        payment.applyGatewayResult(gwRes);

        // 5) 저장
        Payment saved = paymentCommandRepository.save(payment);

        // 6) 응답 변환 (처음 생성 때랑 동일한 DTO 써도 됨)
        return PaymentCreateResponse.from(saved);
    }





}
