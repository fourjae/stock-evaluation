package com.oauth2.payment.domain.port.presentation.dto;


import com.oauth2.constants.payment.PaymentStatus;
import com.oauth2.payment.domain.application.query.PaymentSearchCriteria;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Objects;

@Builder
public record PaymentSearchRequest(
        PaymentStatus paymentStatus,
        OffsetDateTime fromDate,
        OffsetDateTime toDate,
        String customerId,
        Integer page,
        Integer size
) {
    public PaymentSearchCriteria toCriteria() {
        return PaymentSearchCriteria.builder()
                        .paymentStatus(paymentStatus)
                        .fromDate(Objects.isNull(fromDate) ?  OffsetDateTime.now().minusMonths(1) : fromDate)
                        .toDate(Objects.isNull(toDate) ? toDate : OffsetDateTime.now())
                        .customerId(customerId)
                        .page(page)
                        .size(size)
                        .build();
    }
}
