package com.zosh.repository;

import com.zosh.modal.CustomerPayment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CustomerPaymentRepository extends JpaRepository<CustomerPayment, Long> {

    Page<CustomerPayment> findByCustomerId(Long customerId, Pageable pageable);

    Page<CustomerPayment> findByCashierId(Long cashierId, Pageable pageable);
}
