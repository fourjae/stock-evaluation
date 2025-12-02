package com.oauth2.payment.domain.infrastructure.persistence;

import com.oauth2.constants.payment.PaymentStatus;
import com.oauth2.payment.domain.Payment;
import com.oauth2.payment.domain.QPayment;
import com.oauth2.payment.domain.application.query.PaymentListView;
import com.oauth2.payment.domain.application.query.PaymentSearchCriteria;
import com.oauth2.payment.domain.port.out.PaymentCommandRepositoryPort;
import com.oauth2.payment.domain.port.out.PaymentQueryRepositoryPort;
import com.oauth2.payment.domain.port.presentation.dto.PaymentDetailCriteria;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static com.oauth2.payment.domain.QPayment.payment;

@Repository
@RequiredArgsConstructor
public class PaymentQueryRepositoryImpl implements PaymentQueryRepositoryPort {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<PaymentListView> findPayments(PaymentSearchCriteria cond, Pageable pageable) {

        // ------- 메인 데이터 조회 -------
        List<PaymentListView> content = queryFactory
                .select(Projections.constructor(
                        PaymentListView.class,
                        payment.id,
                        payment.customerId,
                        payment.amount,
                        payment.paymentStatus,
                        payment.paidAt,
                        payment.createdAt
                ))
                .from(payment)
                .where(
                        customerIdEq(cond.customerId()),
                        statusEq(cond.paymentStatus()),
                        paidAtBetween(cond.fromDate(), cond.toDate())
                )
                .orderBy(payment.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // ------- count -------
        Long total = queryFactory
                .select(payment.count())
                .from(payment)
                .where(
                        customerIdEq(cond.customerId()),
                        statusEq(cond.paymentStatus()),
                        paidAtBetween(cond.fromDate(), cond.toDate())
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }


    @Override
    public Optional<Payment> findDetail(PaymentDetailCriteria criteria) {
        Payment result = queryFactory
                .selectFrom(payment)
                .where(
                        customerIdEq(criteria.userId()),
                        paymentKeyOrOrderIdEq(criteria)
                )
                .fetchOne();

        return Optional.ofNullable(result);
    }



    private BooleanExpression customerIdEq(String customerId) {
        return customerId != null ? payment.customerId.eq(customerId) : null;
    }

    private BooleanExpression statusEq(PaymentStatus status) {
        return status != null ? payment.paymentStatus.eq(status) : null;
    }

    private BooleanExpression paidAtBetween(OffsetDateTime from, OffsetDateTime to) {
        if (from != null && to != null) {
            return payment.paidAt.between(from, to);
        }
        if (from != null) {
            return payment.paidAt.goe(from);
        }
        if (to != null) {
            return payment.paidAt.loe(to);
        }
        return null;
    }

    private BooleanExpression paymentKeyOrOrderIdEq(PaymentDetailCriteria criteria) {
        if (criteria.paymentKey() != null) {
            return payment.paymentKey.eq(criteria.paymentKey());
        }
        if (criteria.orderId() != null) {
            return payment.orderId.eq(criteria.orderId());
        }
        return null;
    }
}
