package com.oauth2.payment.domain.port.out;

import com.oauth2.payment.domain.Payment;
import com.oauth2.payment.domain.application.query.PaymentSearchCriteria;

import java.util.Optional;

public interface PaymentCommandRepositoryPort {

    Payment save(Payment p);

    Optional<Payment> findById(UUID paymentId);
}
