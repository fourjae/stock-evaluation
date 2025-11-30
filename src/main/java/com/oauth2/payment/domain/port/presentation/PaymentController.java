package com.oauth2.payment.domain.port.presentation;

import com.oauth2.dto.ApiResponse;
import com.oauth2.dto.response.PaymentCreateResponse;
import com.oauth2.dto.response.PaymentDetailResponse;
import com.oauth2.dto.response.PaymentSummaryResponse;
import com.oauth2.payment.domain.application.PaymentService;
import com.oauth2.payment.domain.port.presentation.dto.ChargePaymentRequest;
import com.oauth2.payment.domain.port.presentation.dto.PaymentDetailCriteria;
import com.oauth2.payment.domain.port.presentation.dto.PaymentSearchRequest;
import com.oauth2.security.CustomUserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * 결제 생성 & 실행
     */
    @PostMapping
    public ApiResponse<PaymentCreateResponse> executePayment(
            @RequestHeader(value = "Idempotency-Key", required = false) String idemKey,
            @Valid @RequestBody ChargePaymentRequest req
    ) {
        PaymentCreateResponse paymentResponse = paymentService.executePayment(req.toCommand(idemKey));
        return ApiResponse.ok(paymentResponse);
    }

    /**
     * 결제 목록 조회
     */
    @GetMapping("/payments")
    public ApiResponse<Page<PaymentSummaryResponse>> getPayments(
            @Valid PaymentSearchRequest req,
            Pageable pageable
    ) {
        Page<PaymentSummaryResponse> paymentResponse = paymentService.getPayments(req.toCriteria(), pageable);
        return ApiResponse.ok(paymentResponse);
    }

    /**
     * 결제 단건 조회
     */
    @GetMapping("/{paymentKey}")
    public ApiResponse<PaymentDetailResponse> getPayment(
            @PathVariable String paymentKey,
            @AuthenticationPrincipal CustomUserPrincipal user // 로그인 사용자
    ) {
        PaymentDetailResponse paymentDetailResponse = paymentService.getPayment(
                PaymentDetailCriteria.builder()
                        .paymentKey(paymentKey)
                        .userId(user.getUserId())
                        .build()
        );
        return ApiResponse.ok(paymentDetailResponse);
    }

}
