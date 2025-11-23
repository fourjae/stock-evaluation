package com.oauth2.payment.domain;

import com.oauth2.constants.payment.PaymentStatus;
import com.oauth2.payment.domain.application.dto.ChargePaymentCommand;
import com.oauth2.payment.domain.port.out.dto.GatewayChargeResult;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

// com.oauth2.payment.domain
@Entity
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE) // 빌더용
public class Payment {
    @Id
    @GeneratedValue
    private UUID id;

    private String gatewayPaymentId;

    @Column(nullable=false)
    private BigDecimal amount;

    @Column(nullable=false,length=3)
    private String currency;
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    private String customerId;
    private String methodId;
    private String failureCode;
    private String failureMessage;
    private OffsetDateTime paidAt;

    private OffsetDateTime createdAt;
    private String createdBy;
    private OffsetDateTime updatedAt;
    private String updatedBy;


    public void applyGatewayResult(GatewayChargeResult gw) {
        if (this.status != PaymentStatus.PENDING) {
            throw new IllegalStateException("PENDING 상태에서만 결제 결과를 반영할 수 있습니다. current=" + status);
        }

        this.gatewayPaymentId = gw.gatewayPaymentId();
        this.status = gw.status();      // 필요 없으면 제거
        this.failureCode = gw.failureCode();
        this.failureMessage = gw.failureMessage();

        if (gw.succeeded()) {
            this.status = PaymentStatus.SUCCEEDED;
            this.paidAt = OffsetDateTime.now();
        } else {
            this.status = PaymentStatus.FAILED;
        }
    }

}

