package com.oauth2.dto.request.payment;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentCommand {
    private Long amount;              // 금액
    private String currency;          // 통화
    private String customerId;        // 소비자 ID
    private String paymentMethodId;   // 결제 방법
}
