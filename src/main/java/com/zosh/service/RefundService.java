package com.zosh.service;

import com.zosh.domain.RefundStatus;
import com.zosh.domain.PaymentType;
import com.zosh.exception.UserException;
import com.zosh.payload.dto.OrderDTO;
import com.zosh.payload.dto.RefundDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface RefundService {
    RefundDTO createRefund(RefundDTO dto) throws UserException;
    RefundDTO getRefundById(Long id);
    List<RefundDTO> getRefundsByOrder(Long orderId);
    Page<RefundDTO> getRefundsByCustomerIdPagin(
            Long customerId,
            LocalDateTime start,
            LocalDateTime end,
            String search,
            Pageable pageable
    );
    List<RefundDTO> getRefundsByBranch(Long branchId,
                                     Long customerId,
                                     Long cashierId,
                                     PaymentType paymentType,
                                     RefundStatus status);

    // ← CHANGE THIS
    Page<RefundDTO> getRefundsByCashier(
            Long cashierId,
            LocalDateTime start,
            LocalDateTime end,
            String search,
            Pageable pageable
    );

    void deleteRefund(Long id);
    List<RefundDTO> getTodayRefundsByBranch(Long branchId);
    List<RefundDTO> getRefundsByCustomerId(Long customerId);
    List<RefundDTO> getTop5RecentRefundsByBranchId(Long branchId);

}
