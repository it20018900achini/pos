package com.zosh.service;

import com.zosh.payload.dto.CustomerPaymentDTO;
import com.zosh.modal.PaymentMethod;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public interface CustomerPaymentService {

    CustomerPaymentDTO createPayment(CustomerPaymentDTO dto);

    CustomerPaymentDTO updatePayment(Long id, CustomerPaymentDTO dto);

    void deletePayment(Long id);

    CustomerPaymentDTO getById(Long id);

    Page<CustomerPaymentDTO> getPayments(Long customerId, Long cashierId, PaymentMethod paymentMethod,
                                         LocalDateTime startDate, LocalDateTime endDate,
                                         String sortBy, String sortDir, int page, int size);

    List<CustomerPaymentDTO> getByCustomerId(Long customerId);

    Page<CustomerPaymentDTO> getByCustomerId(Long customerId, int page, int size, String sortBy, String sortDir);
}
