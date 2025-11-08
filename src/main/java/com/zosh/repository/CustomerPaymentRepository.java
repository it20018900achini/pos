package com.zosh.repository;
import com.zosh.modal.CustomerPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CustomerPaymentRepository extends JpaRepository<CustomerPayment, Long> {

    List<CustomerPayment> findByCustomerId(Long customerId);
    List<CustomerPayment> findByCashierId(Long cashierId);
}
