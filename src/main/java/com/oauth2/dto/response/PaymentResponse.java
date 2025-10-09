package com.oauth2.dto.response;

import com.oauth2.constants.payment.PaymentStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentResponse {
    private String paymentId;
    private PaymentStatus paymentStatus;
}
