package com.oauth2.payment.domain.presentation.dto;


import com.oauth2.constants.payment.PaymentStatus;
import com.oauth2.payment.domain.application.query.PaymentSearchCriteria;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record PaymentSearchRequest(
        PaymentStatus status,
        LocalDateTime fromDate,
        LocalDateTime toDate,
        Long memberId,
        Integer page,
        Integer size
) {
    public PaymentSearchCriteria toCriteria() {
        return PaymentSearchCriteria.builder()
                        .status(status)
                        .fromDate(fromDate)
                        .toDate(toDate)
                        .memberId(memberId)
                        .page(page)
                        .size(size)
                        .build();
    }
}
