package com.zosh.controller;

import com.zosh.payload.dto.CustomerPaymentDTO;
import com.zosh.modal.PaymentMethod;
import com.zosh.service.CustomerPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/customer-payments")
@RequiredArgsConstructor
public class CustomerPaymentController {

    private final CustomerPaymentService service;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    // -------------------- CRUD --------------------

    @PreAuthorize("hasAuthority('ROLE_CASHIER')")
    // Create payment
    @PostMapping
    public ResponseEntity<CustomerPaymentDTO> create(@RequestBody CustomerPaymentDTO dto) {
        return ResponseEntity.ok(service.createPayment(dto));
    }

    // Update payment
    @PutMapping("/{id}")
    public ResponseEntity<CustomerPaymentDTO> update(@PathVariable Long id, @RequestBody CustomerPaymentDTO dto) {
        return ResponseEntity.ok(service.updatePayment(id, dto));
    }

    // Delete payment
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deletePayment(id);
        return ResponseEntity.noContent().build();
    }

    // Get payment by ID
    @GetMapping("/{id}")
    public ResponseEntity<CustomerPaymentDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    // -------------------- Get by Customer --------------------

    // Get all payments by customer
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<CustomerPaymentDTO>> getByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(service.getByCustomerId(customerId));
    }

    // Get paginated payments by customer
    @GetMapping("/customer/{customerId}/paginated")
    public ResponseEntity<Page<CustomerPaymentDTO>> getByCustomerPaginated(
            @PathVariable Long customerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        return ResponseEntity.ok(service.getByCustomerId(customerId, page, size, sortBy, sortDir));
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

        try {
            if (startDate != null) start = LocalDateTime.parse(startDate, formatter);
            if (endDate != null) end = LocalDateTime.parse(endDate, formatter);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

        Page<CustomerPaymentDTO> payments = service.getPayments(customerId, cashierId, paymentMethod,
                start, end, sortBy, sortDir, page, size);

        return ResponseEntity.ok(payments);
    }
}
