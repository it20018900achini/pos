package com.zosh.controller;

import com.zosh.payload.dto.CustomerFullSummaryDTO;
import com.zosh.service.CustomerSummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers/summary")
@RequiredArgsConstructor
public class CustomerSummaryController {

    private final CustomerSummaryService summaryService;

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerFullSummaryDTO> getCustomerSummary(@PathVariable Long customerId) {
        CustomerFullSummaryDTO summary = summaryService.getCustomerFullSummary(customerId);
        return ResponseEntity.ok(summary);
    }
}
