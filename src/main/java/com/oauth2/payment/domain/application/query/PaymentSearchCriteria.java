package com.oauth2.payment.domain.application.query;

import com.oauth2.constants.payment.PaymentStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PaymentSearchCriteria(
        PaymentStatus status,
        LocalDateTime fromDate,
        LocalDateTime toDate,
        Long memberId,
        Integer page,
        Integer size
) {}

