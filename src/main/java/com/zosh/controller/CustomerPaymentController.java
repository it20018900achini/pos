package com.zosh.controller;

import com.zosh.payload.dto.CustomerPaymentDTO;
import com.zosh.service.CustomerPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer-payments")
@RequiredArgsConstructor
public class CustomerPaymentController {

    private final CustomerPaymentService service;

    @PostMapping
    public ResponseEntity<CustomerPaymentDTO> create(@RequestBody CustomerPaymentDTO dto) {
        return ResponseEntity.ok(service.createPayment(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerPaymentDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.getPaymentById(id));
    }

    @GetMapping
    public ResponseEntity<List<CustomerPaymentDTO>> getAll() {
        return ResponseEntity.ok(service.getAllPayments());
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<CustomerPaymentDTO>> getByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(service.getPaymentsByCustomerId(customerId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerPaymentDTO> update(
            @PathVariable Long id,
            @RequestBody CustomerPaymentDTO dto) {
        return ResponseEntity.ok(service.updatePayment(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.deletePayment(id);
        return ResponseEntity.ok("Deleted");
    }
}
