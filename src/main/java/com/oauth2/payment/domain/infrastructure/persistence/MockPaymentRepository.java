package com.oauth2.payment.domain.infrastructure.persistence;

import com.oauth2.payment.domain.port.out.PaymentRepositoryPort;
import org.springframework.stereotype.Repository;

@Repository
public class MockPaymentRepository implements PaymentRepositoryPort {

    public void charge() {

    }
}
