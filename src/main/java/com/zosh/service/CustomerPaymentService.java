package com.zosh.service;

import com.zosh.modal.CustomerPayment;
import com.zosh.modal.PaymentMethod;
import com.zosh.payload.dto.CustomerPaymentDTO;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public interface CustomerPaymentService {

    CustomerPaymentDTO createPayment(CustomerPaymentDTO dto);

    CustomerPaymentDTO updatePayment(Long id, CustomerPaymentDTO dto);

    void deletePayment(Long id);

    CustomerPaymentDTO getPaymentById(Long id);

    List<CustomerPaymentDTO> getAllPayments();

    // ⭐ FIXED: REQUIRED METHOD
    List<CustomerPaymentDTO> getPaymentsByCustomerId(Long customerId);
    Page<CustomerPaymentDTO> getPayments(Long customerId, Long cashierId, PaymentMethod paymentMethod,
                                         LocalDateTime startDate, LocalDateTime endDate,
                                         String sortBy, String sortDir, int page, int size);

}
