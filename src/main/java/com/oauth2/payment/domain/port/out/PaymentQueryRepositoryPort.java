package com.oauth2.payment.domain.port.out;

import com.oauth2.payment.domain.Payment;
import com.oauth2.payment.domain.application.query.PaymentListView;
import com.oauth2.payment.domain.application.query.PaymentSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface PaymentQueryRepositoryPort {
    Page<PaymentListView> findPayments(PaymentSearchCriteria cond, Pageable pageable);
}
