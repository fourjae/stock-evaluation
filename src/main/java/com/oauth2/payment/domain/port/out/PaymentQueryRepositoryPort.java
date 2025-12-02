package com.oauth2.payment.domain.port.out;

import com.oauth2.payment.domain.Payment;
import com.oauth2.payment.domain.application.query.PaymentListView;
import com.oauth2.payment.domain.application.query.PaymentSearchCriteria;
import com.oauth2.payment.domain.port.presentation.dto.PaymentDetailCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;


public interface PaymentQueryRepositoryPort {
    Page<PaymentListView> findPayments(PaymentSearchCriteria cond, Pageable pageable);
    Optional<Payment> findByPaymentKeyAndCustomerId(String paymentKey, String customerId);
    Optional<Payment> findDetail(PaymentDetailCriteria criteria);
}
