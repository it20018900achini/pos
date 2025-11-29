package com.zosh.repository;

import com.zosh.payload.dto.BranchTransactionDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface BranchTransactionRepositoryCustom {
    List<BranchTransactionDTO> findMergedTransactions(
            List<Long> branchIds,
            LocalDateTime startDate,
            LocalDateTime endDate,
            String paymentType,
            int page,
            int size
    );
}
