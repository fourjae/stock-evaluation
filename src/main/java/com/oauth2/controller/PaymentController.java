package com.oauth2.controller;

import com.oauth2.dto.ApiResponse;
import com.oauth2.dto.request.payment.PaymentCommand;
import com.oauth2.dto.response.PaymentResponse;
import com.oauth2.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * 결제 생성 & 실행
     */
    @PostMapping("")
    public ApiResponse<PaymentResponse> executePayment(
            @RequestHeader(value = "Idempotency-Key", required = false) String idemKey,
            @Valid @RequestBody PaymentCommand request
    ) {
        PaymentResponse paymentResponse = paymentService.executePayment();
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
