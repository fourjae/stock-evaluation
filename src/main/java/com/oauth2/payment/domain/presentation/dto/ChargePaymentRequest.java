package com.oauth2.payment.domain.presentation.dto;

import com.oauth2.payment.domain.application.query.ChargePaymentCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Map;

public record ChargePaymentRequest(
        @NotNull(message = "{amount.required}") BigDecimal amount,
        @NotBlank(message = "{currency.required}") String currency,
        @NotBlank(message = "{paymentMethodId.required}") String paymentMethodId,
        String customerId,
        Map<String, String> metadata
) {
    /** 애플리케이션 계층으로 전달할 Command 생성 */
    public ChargePaymentCommand toCommand(String idemKey) {
        return ChargePaymentCommand.builder()
                .amount(amount)
                .currency(currency)
                .paymentMethodId(paymentMethodId)
                .customerId(customerId)
                .metadata(metadata)
                .idemKey(idemKey)
                .build();
    }
}