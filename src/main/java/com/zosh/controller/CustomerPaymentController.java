package com.zosh.controller;

//import com.zosh.payment.dto.CustomerPaymentCreateDTO;
//import com.zosh.payment.model.CustomerPayment;
//import com.zosh.payment.service.CustomerPaymentService;
import com.zosh.modal.CustomerPayment;
import com.zosh.modal.CustomerPaymentCreateDTO;
import com.zosh.service.CustomerPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer-payments")
@RequiredArgsConstructor
public class CustomerPaymentController {

    private final CustomerPaymentService service;

    @PostMapping
    public CustomerPayment create(@RequestBody CustomerPaymentCreateDTO dto) {
        return service.createPayment(dto);
    }

    @GetMapping
    public Page<CustomerPayment> getAll(Pageable pageable) {
        return service.getAll(pageable);
    }

    @GetMapping("/customer/{customerId}")
    public Page<CustomerPayment> getByCustomer(
            @PathVariable Long customerId,
            Pageable pageable
    ) {
        return service.getPaymentsByCustomer(customerId, pageable);
    }

    @GetMapping("/cashier/{cashierId}")
    public Page<CustomerPayment> getByCashier(
            @PathVariable Long cashierId,
            Pageable pageable
    ) {
        return service.getPaymentsByCashier(cashierId, pageable);
    }
}
