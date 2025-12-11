package com.oauth2.payment.domain;

import com.oauth2.constants.payment.PaymentStatus;
import com.oauth2.payment.domain.infrastructure.persistence.converter.MapToJsonConverter;
import com.oauth2.payment.domain.port.out.dto.GatewayChargeResult;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.access.AccessDeniedException;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.*;

import static com.oauth2.constants.payment.PaymentStatus.FAILED;
import static com.oauth2.constants.payment.PaymentStatus.PENDING;
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

    @Convert(converter = MapToJsonConverter.class)
    private Map<String, String> metadata;

    private String customerId;
    private String methodId;
    private String failureCode;
    private String failureMessage;
    private String cancelReason;
    private OffsetDateTime paidAt;

    private OffsetDateTime createdAt;
    private String createdBy;
    private OffsetDateTime updatedAt;
    private String updatedBy;

    public void applyGatewayResult(GatewayChargeResult gw) {
        if (this.paymentStatus != PENDING) {
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
            this.paymentStatus = FAILED;
        }
    }

    public void cancel(String userId, String cancelReason) {
        // 1) 소유자 검증
        if (!this.customerId.equals(String.valueOf(userId))) {
            // 스프링 시큐리티의 AccessDeniedException을 직접 쓰기 싫으면
            // PaymentAccessDeniedException 같은 커스텀 예외 하나 파도 됨
            throw new AccessDeniedException("본인 결제만 취소할 수 있습니다.");
        }

        // 2) 상태 검증
        if (this.paymentStatus == PaymentStatus.CANCELED) {
            throw new IllegalStateException("이미 취소된 결제입니다.");
        }
        if (this.paymentStatus == FAILED) {
            throw new IllegalStateException("실패한 결제는 취소할 수 없습니다.");
        }

        // 3) 상태 변경
        this.paymentStatus = PaymentStatus.CANCELED;
        this.failureCode = "USER_CANCELLED";
        this.cancelReason = cancelReason;
    }

    public void validateRetryable() {
        if (!paymentStatus.isRetryable()) {
            throw new IllegalStateException("현재 상태에서는 결제를 재시도할 수 없습니다. status=" + paymentStatus);
        }
    }


}

