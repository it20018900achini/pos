package com.zosh.repository;

import com.zosh.modal.CustomerPayment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerPaymentRepository extends JpaRepository<CustomerPayment, Long> {

    List<CustomerPayment> findByCustomerId(Long customerId);

    Page<CustomerPayment> findAll(Specification<CustomerPayment> spec, Pageable pageable);
}
