package com.zosh.repository;

import com.zosh.modal.CustomerPayment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
public interface CustomerPaymentRepository extends JpaRepository<CustomerPayment, Long> {

    @Query("""
        SELECT 
            COALESCE(SUM(p.amount),0),
            COALESCE(SUM(CASE WHEN p.paymentMethod = 'CASH' THEN p.amount ELSE 0 END),0),
            COALESCE(SUM(CASE WHEN p.paymentMethod = 'CARD' THEN p.amount ELSE 0 END),0)
        FROM CustomerPayment p
        WHERE p.customer.id = :customerId
    """)
    List<Object[]> getPaymentSummary(@Param("customerId") Long customerId);


    List<CustomerPayment> findByCustomerId(Long customerId);

    Page<CustomerPayment> findAll(Specification<CustomerPayment> spec, Pageable pageable);
}
