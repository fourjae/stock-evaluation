package com.oauth2.constants.payment;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PaymentStatus {
    REQUEST("요청"),
    PENDING("보류"),
    SUCCEEDED("성공"),
    CANCELED("취소"),
    FAILED("실패")
    ;

    private final String name;
}
