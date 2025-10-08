package com.oauth2.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentResponse {
    private String paymentId;
    private String paymentStatus;
}
