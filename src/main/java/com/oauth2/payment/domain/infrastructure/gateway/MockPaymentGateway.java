package com.oauth2.payment.domain.infrastructure.gateway;

import com.oauth2.payment.domain.port.out.PaymentGateway;
import com.oauth2.payment.domain.port.out.dto.GatewayChargeRequest;
import com.oauth2.payment.domain.port.out.dto.GatewayChargeResult;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Component
public class MockPaymentGateway implements PaymentGateway {
    public GatewayChargeResult charge(GatewayChargeRequest req) {

        if (req.amount().compareTo(BigDecimal.ZERO) <= 0) {
            return GatewayChargeResult.builder()
                    .succeeded(false)
                    .gatewayPaymentId(null)
                    .status("FAILED")
                    .failureCode("MOCK_DECLINED")
                    .failureMessage("테스트 거절 (amount==0)")
                    .raw(Map.of("rule", "amount==0", "idemKey", req.idempotencyKey()))
                    .build();
        }

        // 성공 응답
        String pid = "pay_" + UUID.randomUUID();
        return GatewayChargeResult.builder()
                .succeeded(true)
                .gatewayPaymentId(pid)
                .status("SUCCEEDED")
                .failureCode(null)
                .failureMessage(null)
                .raw(Map.of("idemKey", req.idempotencyKey(), "methodId", req.paymentMethodId()))
                .build();
    }
}
