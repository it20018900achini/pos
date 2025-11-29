package com.zosh.service.impl;

import com.zosh.payload.dto.BranchTransactionDTO;
import com.zosh.repository.BranchTransactionRepositoryCustom;
import com.zosh.service.BranchTransactionService;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BranchTransactionServiceImpl implements BranchTransactionService {

    private final BranchTransactionRepositoryCustom transactionRepository;

    public BranchTransactionServiceImpl(BranchTransactionRepositoryCustom transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public List<BranchTransactionDTO> getMergedTransactions(List<Long> branchIds,
                                                            LocalDateTime startDate,
                                                            LocalDateTime endDate,
                                                            String paymentType,
                                                            int page,
                                                            int size) {
        return transactionRepository.findMergedTransactions(branchIds, startDate, endDate, paymentType, page, size);
    }

    @Override
    public List<BranchTransactionDTO> getAllMergedTransactions(List<Long> branchIds,
                                                               LocalDateTime startDate,
                                                               LocalDateTime endDate,
                                                               String paymentType) {
        return transactionRepository.findMergedTransactions(branchIds, startDate, endDate, paymentType, 0, 0);
    }
}
