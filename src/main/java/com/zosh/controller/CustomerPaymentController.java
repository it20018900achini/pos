package com.zosh.controller;

import com.zosh.modal.CustomerPaymentCreateDTO;
import com.zosh.service.CustomerPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer-payments")
@RequiredArgsConstructor
public class CustomerPaymentController {

    private final com.zosh.service.CustomerPaymentService service;

    @PostMapping

    public Object create(@RequestBody com.zosh.modal.CustomerPaymentCreateDTO dto) {
        return service.createPayment(dto);
    }

    @GetMapping("/customer/{customerId}")
    public Object getByCustomer(@PathVariable Long customerId) {
        return service.getPaymentsByCustomer(customerId);
    }

    @GetMapping("/cashier/{cashierId}")
    public Object getByCashier(@PathVariable Long cashierId) {
        return service.getPaymentsByCashier(cashierId);
    }
}