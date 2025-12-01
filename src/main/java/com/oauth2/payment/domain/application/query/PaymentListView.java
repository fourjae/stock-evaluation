package com.oauth2.payment.domain.application.query;


import com.oauth2.constants.payment.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
public class PaymentListView {

    private final String paymentKey;
    private final String customerId;
    private final BigDecimal amount;
    private final PaymentStatus paymentStatus;
    private final OffsetDateTime paidAt;
    private final OffsetDateTime createdAt;

}
