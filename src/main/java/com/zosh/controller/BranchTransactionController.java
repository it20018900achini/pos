package com.zosh.controller;
import com.zosh.payload.dto.BranchTransactionDTO;
import com.zosh.service.BranchTransactionService;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class BranchTransactionController {

    private final BranchTransactionService transactionService;

    public BranchTransactionController(BranchTransactionService transactionService) {
        this.transactionService = transactionService;
    }

    // Paged endpoint
    @GetMapping("/merged")
    public List<BranchTransactionDTO> getMergedTransactions(
            @RequestParam List<Long> branchIds,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String paymentType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        LocalDateTime start = startDate != null ? LocalDateTime.parse(startDate) : null;
        LocalDateTime end = endDate != null ? LocalDateTime.parse(endDate) : null;
        return transactionService.getMergedTransactions(branchIds, start, end, paymentType, page, size);
    }

    // Fetch all
    @GetMapping("/merged/all")
    public List<BranchTransactionDTO> getAllMergedTransactions(
            @RequestParam List<Long> branchIds,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String paymentType
    ) {
        LocalDateTime start = startDate != null ? LocalDateTime.parse(startDate) : null;
        LocalDateTime end = endDate != null ? LocalDateTime.parse(endDate) : null;
        return transactionService.getAllMergedTransactions(branchIds, start, end, paymentType);
    }
}

