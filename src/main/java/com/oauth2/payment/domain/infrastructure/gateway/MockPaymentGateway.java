package com.oauth2.payment.domain.infrastructure.gateway;

import com.oauth2.payment.domain.port.out.PaymentGateway;
import com.oauth2.payment.domain.port.out.dto.GatewayChargeRequest;
import org.springframework.stereotype.Component;

@Component
public class MockPaymentGateway implements PaymentGateway {
    public void charge(GatewayChargeRequest req) {

    }
}
