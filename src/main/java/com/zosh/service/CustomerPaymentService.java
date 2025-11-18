package com.zosh.service;

import com.zosh.modal.CustomerPayment;
import com.zosh.payload.dto.CustomerPaymentDTO;

import java.util.List;

public interface CustomerPaymentService {

    CustomerPaymentDTO createPayment(CustomerPaymentDTO dto);

    CustomerPaymentDTO updatePayment(Long id, CustomerPaymentDTO dto);

    void deletePayment(Long id);

    CustomerPaymentDTO getPaymentById(Long id);

    List<CustomerPaymentDTO> getAllPayments();

    // ⭐ FIXED: REQUIRED METHOD
    List<CustomerPaymentDTO> getPaymentsByCustomerId(Long customerId);
}
