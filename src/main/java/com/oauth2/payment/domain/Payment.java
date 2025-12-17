package com.oauth2.payment.domain;

import com.oauth2.constants.payment.GatewayPaymentStatus;
import com.oauth2.constants.payment.PaymentStatus;
import com.oauth2.payment.domain.infrastructure.persistence.converter.MapToJsonConverter;
import com.oauth2.payment.domain.port.out.dto.GatewayChargeResult;
import com.oauth2.payment.domain.port.out.dto.GatewayPaymentStatusResult;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.access.AccessDeniedException;

import java.math.BigDecimal;
import java.time.Instant;
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

    private String gatewayRawStatus;
    private Instant gatewayLastSyncedAt;


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

    public void syncWithGatewayStatus(GatewayPaymentStatusResult pg) {
        validateGatewayId(pg);

        // 관측값 기록
        this.gatewayRawStatus = pg.rawStatus();
        this.gatewayLastSyncedAt = Instant.now();

        // PG 상태를 "우리 상태"로 변환
        PaymentStatus target = mapToPaymentStatus(pg.status());

        // 이미 같은 상태면 부가필드만 최신화/정리하고 끝(멱등)
        if (this.paymentStatus == target) {
            normalizeSideFields(target, pg);
            return;
        }

        // ✅ PG가 진실: final이든 뭐든 target으로 강제 수렴
        this.paymentStatus = target;

        // 상태에 맞춰 부가 필드 정리/세팅
        normalizeSideFields(target, pg);
    }

    private void validateGatewayId(GatewayPaymentStatusResult pg) {
        if (!Objects.equals(this.gatewayPaymentId, pg.gatewayPaymentId())) {
            throw new IllegalArgumentException("gatewayPaymentId mismatch");
        }
    }

    private PaymentStatus mapToPaymentStatus(GatewayPaymentStatus s) {
        return switch (s) {
            case READY -> PaymentStatus.READY;
            case IN_PROGRESS -> PaymentStatus.IN_PROGRESS;
            case APPROVED -> PaymentStatus.PAID;
            case CANCELED -> PaymentStatus.CANCELED;
            case FAILED, EXPIRED -> PaymentStatus.FAILED;
            case UNKNOWN -> this.status; // UNKNOWN이면 상태 변경 안 함(정책)
        };
    }


    /**
     * 상태에 따라 paidAt/canceledAt/failureReason 같은 "파생 필드"를 정리
     * - PG가 진실이므로 이전에 찍혀있던 값이 있어도 target 기준으로 재정렬
     */
    private void normalizeSideFields(PaymentStatus target, GatewayPaymentStatusResult pg) {
        switch (target) {
            case PAID -> {
                this.paidAt = (pg.approvedAt() != null) ? pg.approvedAt() : defaultNowIfNull(this.paidAt);
                this.canceledAt = null;         // 결제완료면 취소시각 제거(단순 모델 기준)
                this.failureReason = null;
            }
            case CANCELED -> {
                this.canceledAt = (pg.canceledAt() != null) ? pg.canceledAt() : defaultNowIfNull(this.canceledAt);
                // 취소면 보통 paidAt 유지/삭제 정책이 갈림:
                // - "승인 후 전체취소" 케이스까지 표현하려면 paidAt 유지가 더 자연스러움.
                // - 단순 모델이면 null로 비워도 됨.
                // 여기선 유지(승인됐다가 취소된 흐름을 보존)로 둠.
                this.failureReason = null;
            }
            case FAILED -> {
                this.failureReason = pg.reason();
                // 실패는 paid/cancel 시각 제거
                this.paidAt = null;
                this.canceledAt = null;
            }
            case READY, IN_PROGRESS -> {
                // 진행중/준비중으로 돌아갈 수도 있으니 관련 필드 정리
                this.failureReason = null;
                this.canceledAt = null;
                // paidAt은 "승인 아님"이므로 제거(정합성 우선)
                this.paidAt = null;
            }
        }
    }

    private Instant defaultNowIfNull(Instant current) {
        return (current != null) ? current : Instant.now();
    }



}

