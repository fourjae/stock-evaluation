package com.oauth2.payment.domain.port.out;

import com.oauth2.payment.domain.Payment;

public interface PaymentRepositoryPort {
    Payment save(Payment p);
}
