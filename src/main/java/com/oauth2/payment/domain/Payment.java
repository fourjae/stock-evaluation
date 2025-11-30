package com.oauth2.payment.domain;

import com.oauth2.constants.payment.PaymentStatus;
import com.oauth2.payment.domain.port.out.dto.GatewayChargeResult;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.*;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                    // 내부 PK

    @Column(nullable = false, unique = true, length = 36)
    private String paymentKey;          // 외부 노출용 UUID 문자열

    @Column(nullable = false, unique = true, length = 36)
    private String gatewayPaymentId;    // 외부 PG사 결제 트랜잭션 ID

    @Column(nullable=false)
    private BigDecimal amount;

    @Column(nullable=false,length=3)
    private String currency;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
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
        if (this.paymentStatus != PaymentStatus.PENDING) {
            throw new IllegalStateException("PENDING 상태에서만 결제 결과를 반영할 수 있습니다. current=" + paymentStatus);
        }

        this.paymentKey = gw.paymentKey();
        this.paymentStatus = gw.paymentStatus();      // 필요 없으면 제거
        this.failureCode = gw.failureCode();
        this.failureMessage = gw.failureMessage();

        if (gw.succeeded()) {
            this.paymentStatus = PaymentStatus.SUCCEEDED;
            this.paidAt = OffsetDateTime.now();
        } else {
            this.paymentStatus = PaymentStatus.FAILED;
        }
    }

}

