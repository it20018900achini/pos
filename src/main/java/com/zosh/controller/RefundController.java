package com.zosh.controller;

import com.zosh.domain.OrderStatus;
import com.zosh.domain.RefundStatus;
import com.zosh.domain.PaymentType;
import com.zosh.exception.UserException;
import com.zosh.payload.dto.OrderDTO;
import com.zosh.payload.dto.RefundDTO;
import com.zosh.service.RefundService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
@RestController
@RequestMapping("/api/refunds")
@RequiredArgsConstructor
public class RefundController {

    private final RefundService refundService;

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_CASHIER')")
    public ResponseEntity<RefundDTO> createRefund(@RequestBody RefundDTO dto) throws UserException {
        return ResponseEntity.ok(refundService.createRefund(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RefundDTO> getRefund(@PathVariable Long id) {
        return ResponseEntity.ok(refundService.getRefundById(id));
    }

    @GetMapping("/branch/{branchId}")
    public ResponseEntity<List<RefundDTO>> getRefundsByBranch(
            @PathVariable Long branchId,
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) Long cashierId,
            @RequestParam(required = false) PaymentType paymentType,
            @RequestParam(required = false) RefundStatus status) {
        return ResponseEntity.ok(refundService.getRefundsByBranch(
                        branchId,
                        customerId,
                        cashierId,
                        paymentType,
                        status
                )
        );
    }
    @GetMapping("/branch/t/{branchId}")
    public ResponseEntity<Page<RefundDTO>> getRefundsByBranchPagin(
            @PathVariable Long branchId,
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) Long cashierId,
            @RequestParam(required = false) PaymentType paymentType,
            @RequestParam(required = false) RefundStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam(required = false) String search,
            Pageable pageable



    ) {
        return ResponseEntity.ok(refundService.getRefundsByBranchPagin(
                        branchId,
                        customerId,
                        cashierId,
                        paymentType,
                        status,
                start,
                end,
                search,
                pageable
                )
        );
    }

    @GetMapping("/refund/{refundId}")
    public ResponseEntity<List<RefundDTO>> getRefundsByBranchD(
            @PathVariable Long refundId,
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) Long cashierId,
            @RequestParam(required = false) PaymentType paymentType,
            @RequestParam(required = false) RefundStatus status) {
        return ResponseEntity.ok(refundService.getRefundsByBranch(
                        refundId,
                        customerId,
                        cashierId,
                        paymentType,
                        status
                )
        );
    }
    @GetMapping("/cashier/{cashierId}")
    public ResponseEntity<Page<RefundDTO>> getRefundsByCashier(
            @PathVariable Long cashierId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam(required = false) String search,
            Pageable pageable
    ) {
        Page<RefundDTO> refunds = refundService.getRefundsByCashier(cashierId, start, end, search,pageable);
        return ResponseEntity.ok(refunds);
    }

    @GetMapping("/today/refund/{refundId}")
    public ResponseEntity<List<RefundDTO>> getTodayRefunds(@PathVariable Long refundId) {
        return ResponseEntity.ok(refundService.getTodayRefundsByBranch(refundId));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<RefundDTO>> getCustomerRefunds(@PathVariable Long customerId) {
        return ResponseEntity.ok(refundService.getRefundsByCustomerId(customerId));
    }
    @GetMapping("/customer/t/{customerId}")
    public ResponseEntity<Page<RefundDTO>> getCustomerRefundsPagin(

            @PathVariable Long customerId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam(required = false) String search,
            Pageable pageable

    ) {
//        return ResponseEntity.ok(refundService.getRefundsByCustomerId(customerId));

        Page<RefundDTO> refunds = refundService.getRefundsByCustomerIdPagin(customerId, start, end, search,pageable);
        return ResponseEntity.ok(refunds);


    }

    @GetMapping("/recent/{refundId}")
    @PreAuthorize("hasAnyAuthority('ROLE_BRANCH_MANAGER', 'ROLE_BRANCH_ADMIN')")
    public ResponseEntity<List<RefundDTO>> getRecentRefunds(@PathVariable Long refundId) {
        List<RefundDTO> recentRefunds = refundService.getTop5RecentRefundsByBranchId(refundId);
        return ResponseEntity.ok(recentRefunds);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_STORE_MANAGER') or hasAuthority('ROLE_STORE_ADMIN')")
    public ResponseEntity<Void> deleteRefund(@PathVariable Long id) {
        refundService.deleteRefund(id);
        return ResponseEntity.noContent().build();
    }


}
