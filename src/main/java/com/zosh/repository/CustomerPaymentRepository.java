package com.zosh.repository;

import com.zosh.modal.CustomerPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CustomerPaymentRepository extends JpaRepository<CustomerPayment, Long>,
        JpaSpecificationExecutor<CustomerPayment> {
}
