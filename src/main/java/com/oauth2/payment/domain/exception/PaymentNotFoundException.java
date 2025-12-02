package com.oauth2.payment.domain.exception;

import com.oauth2.common.exception.BusinessException;
import com.oauth2.common.exception.ErrorCode;

public class PaymentNotFoundException extends BusinessException {

    public PaymentNotFoundException(String paymentKey) {
        super(ErrorCode.PAYMENT_NOT_FOUND, "결제를 찾을 수 없습니다. paymentKey=" + paymentKey);
    }
}
