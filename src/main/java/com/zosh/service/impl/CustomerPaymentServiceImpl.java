package com.zosh.service.impl;

import com.zosh.mapper.CustomerPaymentMapper;
import com.zosh.modal.*;
import com.zosh.payload.dto.CustomerPaymentDTO;
import com.zosh.repository.BranchRepository;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerPaymentServiceImpl implements CustomerPaymentService {

    private final CustomerPaymentRepository repository;
    private final CustomerRepository customerRepository;
    private final BranchRepository branchRepository;
    private final UserRepository userRepository;

    @Override
    public CustomerPaymentDTO createPayment(CustomerPaymentDTO dto) {
        CustomerPayment entity = CustomerPaymentMapper.toEntity(dto);

        if (dto.getCustomerId() != null) {
            Customer customer = customerRepository.findById(dto.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Customer not found"));
            entity.setCustomer(customer);
        }

        if (dto.getCashierId() != null) {
            User cashier = userRepository.findById(dto.getCashierId())
                    .orElseThrow(() -> new RuntimeException("Cashier not found"));
            entity.setCashier(cashier);
        }

        if (dto.getBranchId() != null) {
            Branch branch = branchRepository.findById(dto.getBranchId())
                    .orElseThrow(() -> new RuntimeException("Branch not found"));
            entity.setBranch(branch);
        }

        CustomerPayment saved = repository.save(entity);
        return CustomerPaymentMapper.toDto(saved);
    }

    @Override
    public CustomerPaymentDTO updatePayment(Long id, CustomerPaymentDTO dto) {
        CustomerPayment existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        existing.setAmount(dto.getAmount());
        existing.setPaymentMethod(dto.getPaymentMethod());
        existing.setReference(dto.getReference());
        existing.setNote(dto.getNote());

        if (dto.getCustomerId() != null) {
            Customer customer = customerRepository.findById(dto.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Customer not found"));
            existing.setCustomer(customer);
        }

        if (dto.getCashierId() != null) {
            User cashier = userRepository.findById(dto.getCashierId())
                    .orElseThrow(() -> new RuntimeException("Cashier not found"));
            existing.setCashier(cashier);
        }

        if (dto.getBranchId() != null) {
            Branch branch = branchRepository.findById(dto.getBranchId())
                    .orElseThrow(() -> new RuntimeException("Branch not found"));
            existing.setBranch(branch);
        }

        CustomerPayment updated = repository.save(existing);
        return CustomerPaymentMapper.toDto(updated);
    }

    @Override
    public void deletePayment(Long id) {
        repository.deleteById(id);
    }

    @Override
    public CustomerPaymentDTO getPaymentById(Long id) {
        return repository.findById(id)
                .map(CustomerPaymentMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
    }

    @Override
    public List<CustomerPaymentDTO> getAllPayments() {
        return repository.findAll().stream()
                .map(CustomerPaymentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CustomerPaymentDTO> getPaymentsByCustomerId(Long customerId) {
        return repository.findByCustomerId(customerId).stream()
                .map(CustomerPaymentMapper::toDto)
                .collect(Collectors.toList());
    }
    @Override
    public Page<CustomerPaymentDTO> getPayments(Long customerId, Long cashierId, PaymentMethod paymentMethod,
                                                LocalDateTime startDate, LocalDateTime endDate,
                                                String sortBy, String sortDir, int page, int size) {

        // Default sort field and direction
        String sortField = (sortBy != null && !sortBy.isBlank()) ? sortBy : "createdAt";
        Sort.Direction direction = "asc".equalsIgnoreCase(sortDir) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

        // Build specification manually without using Specification.where()
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
        if (startDate != null && endDate != null) {
            Specification<CustomerPayment> s = CustomerPaymentSpecifications.createdBetween(startDate, endDate);
            spec = (spec == null) ? s : spec.and(s);
        }

        Page<CustomerPayment> paymentsPage = repository.findAll(spec, pageable);

        // Map directly to DTOs
        return paymentsPage.map(CustomerPaymentMapper::toDto);
    }


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
}
