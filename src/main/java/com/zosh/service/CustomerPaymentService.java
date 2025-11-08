package com.zosh.service;

//import com.zosh.payment.dto.CustomerPaymentCreateDTO;
//import com.zosh.payment.model.*;
//import com.zosh.payment.repo.CustomerPaymentRepository;
import com.zosh.modal.CustomerPayment;
import com.zosh.modal.CustomerPayment.*;
import com.zosh.modal.CustomerPaymentCreateDTO;
import com.zosh.modal.PaymentMethod;
import com.zosh.repository.CustomerPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerPaymentService {

    private final CustomerPaymentRepository repo;

    public CustomerPayment createPayment(CustomerPaymentCreateDTO dto) {

        PaymentMethod method = PaymentMethod.valueOf(dto.getPaymentMethod().toUpperCase());

        CustomerPayment payment = CustomerPayment.builder()
                .customerId(dto.getCustomerId())
                .cashierId(dto.getCashierId())
                .amount(dto.getAmount())
                .paymentMethod(method)
                .reference(dto.getReference())
                .note(dto.getNote())
                .build();

        return repo.save(payment);
    }

    public List<CustomerPayment> getPaymentsByCustomer(Long customerId) {
        return repo.findByCustomerId(customerId);
    }

    public List<CustomerPayment> getPaymentsByCashier(Long cashierId) {
        return repo.findByCashierId(cashierId);
    }
}