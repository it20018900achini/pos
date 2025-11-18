package com.zosh.service.impl;

import com.zosh.payload.dto.CustomerPaymentDTO;
import com.zosh.modal.PaymentMethod;
import com.zosh.modal.Customer;
import com.zosh.modal.CustomerPayment;
import com.zosh.modal.User;
import com.zosh.repository.CustomerPaymentRepository;
import com.zosh.repository.CustomerRepository;
import com.zosh.repository.UserRepository;
import com.zosh.service.CustomerPaymentService;
import com.zosh.specification.CustomerPaymentSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerPaymentServiceImpl implements CustomerPaymentService {

    private final CustomerPaymentRepository repository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;

    // -------------------- CRUD --------------------

    @Override
    public CustomerPaymentDTO createPayment(CustomerPaymentDTO dto) {
        CustomerPayment payment = new CustomerPayment();
        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        User cashier = userRepository.findById(dto.getCashierId())
                .orElseThrow(() -> new RuntimeException("Cashier not found"));

        payment.setCustomer(customer);
        payment.setCashier(cashier);
        payment.setAmount(dto.getAmount());
        payment.setPaymentMethod(dto.getPaymentMethod());
        payment.setReference(dto.getReference());
        payment.setNote(dto.getNote());

        CustomerPayment saved = repository.save(payment);
        return toDTO(saved);
    }

    @Override
    public CustomerPaymentDTO updatePayment(Long id, CustomerPaymentDTO dto) {
        CustomerPayment payment = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        if (dto.getCustomerId() != null) {
            Customer customer = customerRepository.findById(dto.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Customer not found"));
            payment.setCustomer(customer);
        }
        if (dto.getCashierId() != null) {
            User cashier = userRepository.findById(dto.getCashierId())
                    .orElseThrow(() -> new RuntimeException("Cashier not found"));
            payment.setCashier(cashier);
        }

        payment.setAmount(dto.getAmount());
        payment.setPaymentMethod(dto.getPaymentMethod());
        payment.setReference(dto.getReference());
        payment.setNote(dto.getNote());

        CustomerPayment updated = repository.save(payment);
        return toDTO(updated);
    }

    @Override
    public void deletePayment(Long id) {
        repository.deleteById(id);
    }

    @Override
    public CustomerPaymentDTO getById(Long id) {
        CustomerPayment payment = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        return toDTO(payment);
    }

    // -------------------- Filtering & Pagination --------------------

    @Override
    public Page<CustomerPaymentDTO> getPayments(Long customerId, Long cashierId, PaymentMethod paymentMethod,
                                                LocalDateTime startDate, LocalDateTime endDate,
                                                String sortBy, String sortDir, int page, int size) {
        Pageable pageable = PageRequest.of(page, size,
                Sort.by(sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy));

        Specification<CustomerPayment> spec = buildSpecification(customerId, cashierId, paymentMethod, startDate, endDate);

        return repository.findAll(spec, pageable)
                .map(this::toDTO);
    }

    @Override
    public List<CustomerPaymentDTO> getByCustomerId(Long customerId) {
        Specification<CustomerPayment> spec = CustomerPaymentSpecifications.hasCustomerId(customerId);
        List<CustomerPayment> payments = repository.findAll(spec);
        return payments.stream().map(this::toDTO).toList();
    }

    @Override
    public Page<CustomerPaymentDTO> getByCustomerId(Long customerId, int page, int size, String sortBy, String sortDir) {
        Pageable pageable = PageRequest.of(page, size,
                Sort.by(sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy));
        Specification<CustomerPayment> spec = CustomerPaymentSpecifications.hasCustomerId(customerId);
        return repository.findAll(spec, pageable).map(this::toDTO);
    }

    // -------------------- Utility Methods --------------------

    private Specification<CustomerPayment> buildSpecification(Long customerId, Long cashierId,
                                                              PaymentMethod paymentMethod,
                                                              LocalDateTime start, LocalDateTime end) {
        Specification<CustomerPayment> spec = null;

        if (customerId != null) spec = CustomerPaymentSpecifications.hasCustomerId(customerId);
        if (cashierId != null) {
            Specification<CustomerPayment> s = CustomerPaymentSpecifications.hasCashierId(cashierId);
            spec = (spec == null) ? s : spec.and(s);
        }
        if (paymentMethod != null) {
            Specification<CustomerPayment> s = CustomerPaymentSpecifications.hasPaymentMethod(paymentMethod);
            spec = (spec == null) ? s : spec.and(s);
        }
        if (start != null && end != null) {
            Specification<CustomerPayment> s = CustomerPaymentSpecifications.createdBetween(start, end);
            spec = (spec == null) ? s : spec.and(s);
        }

        return spec;
    }

    // Manual DTO mapping
    private CustomerPaymentDTO toDTO(CustomerPayment payment) {
        if (payment == null) return null;

        return CustomerPaymentDTO.builder()
                .id(payment.getId())
                .customerId(payment.getCustomer() != null ? payment.getCustomer().getId() : null)
                .cashierId(payment.getCashier() != null ? payment.getCashier().getId() : null)
                .amount(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod())
                .reference(payment.getReference())
                .note(payment.getNote())
                .createdAt(payment.getCreatedAt())
                .build();
    }
}
