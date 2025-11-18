package com.zosh.service.impl;

import com.zosh.mapper.CustomerPaymentMapper;
import com.zosh.modal.Branch;
import com.zosh.modal.Customer;
import com.zosh.modal.CustomerPayment;
import com.zosh.modal.User;
import com.zosh.repository.BranchRepository;
import com.zosh.repository.CustomerPaymentRepository;
import com.zosh.payload.dto.CustomerPaymentDTO;
import com.zosh.repository.CustomerRepository;
import com.zosh.repository.UserRepository;
import com.zosh.service.CustomerPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        return CustomerPaymentMapper.toDto(repository.save(entity));
    }

    @Override
    public CustomerPaymentDTO updatePayment(Long id, CustomerPaymentDTO dto) {
        CustomerPayment existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        existing.setAmount(dto.getAmount());
        existing.setPaymentMethod(dto.getPaymentMethod());
        existing.setReference(dto.getReference());
        existing.setNote(dto.getNote());
        // Update Customer (relation)
        if (dto.getCustomerId() != null) {

            Customer customer = customerRepository.findById(dto.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Customer not found"));


//            Customer customer = CustomerRepository.findById(dto.getCustomerId())
//                    .orElseThrow(() -> new RuntimeException("Customer not found"));
            existing.setCustomer(customer);
        }

// Update Cashier (relation)
        if (dto.getCashierId() != null) {
            User cashier = userRepository.findById(dto.getCashierId())
                    .orElseThrow(() -> new RuntimeException("Cashier not found"));
            existing.setCashier(cashier);
        }

// Update Branch (relation, only if branch exists in entity)
        if (dto.getBranchId() != null) {
            Branch branch = branchRepository.findById(dto.getBranchId())
                    .orElseThrow(() -> new RuntimeException("Branch not found"));
            existing.setBranch(branch);
        }

        return CustomerPaymentMapper.toDto(repository.save(existing));
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
        return repository.findAll()
                .stream()
                .map(CustomerPaymentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CustomerPaymentDTO> getPaymentsByCustomerId(Long customerId) {
        return repository.findByCustomerId(customerId)
                .stream()
                .map(CustomerPaymentMapper::toDto)
                .collect(Collectors.toList());
    }
}
