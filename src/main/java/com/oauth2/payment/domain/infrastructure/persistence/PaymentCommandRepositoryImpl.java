package com.oauth2.payment.domain.infrastructure.persistence;

import com.oauth2.payment.domain.Payment;
import com.oauth2.payment.domain.port.out.PaymentCommandRepositoryPort;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentCommandRepositoryImpl implements PaymentCommandRepositoryPort {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    @Override
    public Payment save(Payment payment) {
        return payment;
    }

}
