package com.oauth2.payment.domain;

import com.oauth2.constants.payment.PaymentStatus;
import com.oauth2.payment.domain.application.dto.ChargePaymentCommand;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
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

    @Column(nullable=false)
    private BigDecimal amount;

    @Column(nullable=false,length=3)
    private String currency;
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    private String customerId;
    private String methodId;

    public static Payment newFrom(ChargePaymentCommand c) { ... } // 팩토리
    public void applyGatewayOutcome(boolean succeeded, String pspId, String failureCode) { ... } // 상태 전이
}

