package com.zosh.controller;

import com.zosh.modal.CustomerPaymentCreateDTO;
import com.zosh.service.CustomerPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer-payments")
@RequiredArgsConstructor
public class CustomerPaymentController {

    private final com.zosh.service.CustomerPaymentService service;

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_CASHIER')")

    public Object create(@RequestBody com.zosh.modal.CustomerPaymentCreateDTO dto) {
        return service.createPayment(dto);
    }

    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasAuthority('ROLE_CASHIER')")

    public Object getByCustomer(@PathVariable Long customerId) {
        return service.getPaymentsByCustomer(customerId);
    }

    @GetMapping("/cashier/{cashierId}")
    @PreAuthorize("hasAuthority('ROLE_CASHIER')")

    public Object getByCashier(@PathVariable Long cashierId) {
        return service.getPaymentsByCashier(cashierId);
    }
}