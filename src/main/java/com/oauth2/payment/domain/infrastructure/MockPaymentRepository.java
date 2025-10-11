package com.oauth2.payment.domain.infrastructure;

import com.oauth2.payment.domain.port.out.PaymentGateway;
import com.oauth2.payment.domain.port.out.PaymentRepositoryPort;
import org.springframework.stereotype.Component;

@Component
public class MockPaymentRepository implements PaymentRepositoryPort {

    public void charge() {

    }
}
