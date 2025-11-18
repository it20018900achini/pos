package com.zosh.controller;

import com.zosh.modal.PaymentMethod;
import com.zosh.payload.dto.CustomerPaymentDTO;
import com.zosh.service.CustomerPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.web.bind.annotation.*;



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



    // -------------------- Filtered & Paginated --------------------
    @GetMapping("/filter")
    public ResponseEntity<Page<CustomerPaymentDTO>> filterPayments(
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) Long cashierId,
            @RequestParam(required = false) PaymentMethod paymentMethod,
            @RequestParam(required = false) String startDate, // "yyyy-MM-dd'T'HH:mm:ss"
            @RequestParam(required = false) String endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {

        LocalDateTime start = null;
        LocalDateTime end = null;

        // ✅ Define the formatter
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        try {
            if (startDate != null && !startDate.isBlank()) {
                start = LocalDateTime.parse(startDate, formatter);
            }
            if (endDate != null && !endDate.isBlank()) {
                end = LocalDateTime.parse(endDate, formatter);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build(); // Invalid date format
        }

        Page<CustomerPaymentDTO> payments = service.getPayments(
                customerId, cashierId, paymentMethod,
                start, end, sortBy, sortDir, page, size
        );

        return ResponseEntity.ok(payments);
    }
}
