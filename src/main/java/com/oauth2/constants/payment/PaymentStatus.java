package com.oauth2.constants.payment;

import lombok.RequiredArgsConstructor;

import java.util.EnumSet;

@RequiredArgsConstructor
public enum PaymentStatus {
    REQUEST("요청"),
    PENDING("보류"),
    SUCCEEDED("성공"),
    CANCELED("취소"),
    FAILED("실패")
    ;


    private static final EnumSet<PaymentStatus> RETRYABLE_STATUSES =
            EnumSet.of(FAILED, PENDING);

    private final String name;

    public boolean isRetryable() {
        return RETRYABLE_STATUSES.contains(this);
    }
}
