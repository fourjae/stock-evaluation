package com.oauth2.payment.domain.application.query;

import com.oauth2.constants.payment.PaymentStatus;
import lombok.Builder;

import java.time.OffsetDateTime;

@Builder
public record PaymentSearchCriteria(
        PaymentStatus paymentStatus,
        OffsetDateTime fromDate,
        OffsetDateTime toDate,
        String customerId,
        Integer page,
        Integer size
) {}



