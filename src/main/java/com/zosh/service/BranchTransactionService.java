package com.zosh.service;

import com.zosh.payload.dto.BranchTransactionDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface BranchTransactionService {

    // Paged transactions
    List<BranchTransactionDTO> getMergedTransactions(
            List<Long> branchIds,
            LocalDateTime startDate,
            LocalDateTime endDate,
            String paymentType,
            int page,
            int size
    );

    // Fetch all transactions
    List<BranchTransactionDTO> getAllMergedTransactions(
            List<Long> branchIds,
            LocalDateTime startDate,
            LocalDateTime endDate,
            String paymentType
    );
}
