package com.zosh.service;


//import com.zosh.payment.model.CustomerPayment;
//import com.zosh.payment.repo.CustomerPaymentRepository;
//import com.zosh.payment.dto.CustomerPaymentCreateDTO;
//import com.zosh.payment.model.PaymentMethod;
import com.zosh.modal.CustomerPayment;
import com.zosh.modal.CustomerPaymentCreateDTO;
import com.zosh.modal.PaymentMethod;
import com.zosh.repository.CustomerPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


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

    public Page<CustomerPayment> getPaymentsByCustomer(Long customerId, Pageable pageable) {
        return repo.findByCustomerId(customerId, pageable);
    }

    public Page<CustomerPayment> getPaymentsByCashier(Long cashierId, Pageable pageable) {
        return repo.findByCashierId(cashierId, pageable);
    }

    public Page<CustomerPayment> getAll(Pageable pageable) {
        return repo.findAll(pageable);
    }
}