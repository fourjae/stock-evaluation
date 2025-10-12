package com.oauth2.payment.domain.presentation;

import com.oauth2.dto.ApiResponse;
import com.oauth2.dto.request.payment.PaymentCommand;
import com.oauth2.dto.response.PaymentResponse;
import com.oauth2.payment.domain.application.PaymentService;
import com.oauth2.payment.domain.presentation.dto.ChargePaymentRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * 결제 생성 & 실행
     */
    @PostMapping("")
    public ApiResponse<PaymentResponse> executePayment(
            @RequestHeader(value = "Idempotency-Key", required = false) String idemKey,
            @Valid @RequestBody ChargePaymentRequest req
    ) {
        PaymentResponse paymentResponse = paymentService.executePayment(req.toCommand(idemKey));
        return ApiResponse.ok(paymentResponse);
    }

//    /**
//     * 결제 목록 조회
//     */
//    @GetMapping("")
//    public PaymentResponse execute(
//            @Valid @RequestBody PaymentCommand req
//    ) {
//        Payment p = service.execute(idemKey, req);
//        return toResponse(p);
//    }
//
//    /**
//     * 결제 목록 단건 조회
//     */
//    @GetMapping("/{paymentId}")
//    public PaymentResponse execute(
//            @Valid @RequestBody PaymentCommand req
//    ) {
//        Payment p = service.execute(idemKey, req);
//        return toResponse(p);
//    }
//
//    /**
//     * 결제 취소 요청
//     */
//    @DeleteMapping("")
//    public PaymentResponse executePayment(
//            @RequestHeader(value = "Idempotency-Key", required = false) String idemKey,
//            @Valid @RequestBody PaymentCommand request
//    ) {
//        return
//    }

}
