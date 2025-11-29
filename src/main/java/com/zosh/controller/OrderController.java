package com.zosh.controller;

import com.zosh.domain.OrderStatus;
import com.zosh.domain.PaymentType;
import com.zosh.exception.UserException;
import com.zosh.payload.dto.OrderDTO;
import com.zosh.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.data.domain.Sort;
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_CASHIER')")
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO dto) throws UserException {
        return ResponseEntity.ok(orderService.createOrder(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }


    @GetMapping("/branch/{branchId}")
    public ResponseEntity<List<OrderDTO>> getOrdersByBranch(
            @PathVariable Long branchId,
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) Long cashierId,
            @RequestParam(required = false) PaymentType paymentType,
            @RequestParam(required = false) OrderStatus status) {
        return ResponseEntity.ok(orderService.getOrdersByBranch(
                        branchId,
                        customerId,
                        cashierId,
                        paymentType,
                        status
                )
        );
    }

    @GetMapping("/branch/t/{branchId}")
    public ResponseEntity<Page<OrderDTO>> getOrdersByBranchPagin(
            @PathVariable Long branchId,
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) Long cashierId,
            @RequestParam(required = false) PaymentType paymentType,
            @RequestParam(required = false) OrderStatus status,

            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam(required = false) String search,
            Pageable pageable
    ) {
        return ResponseEntity.ok(orderService.getOrdersByBranchPagin(
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
    @GetMapping("/cashier/{cashierId}")
    public ResponseEntity<Page<OrderDTO>> getOrdersByCashier(
            @PathVariable Long cashierId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam(required = false) String search,
            Pageable pageable
    ) {
        Page<OrderDTO> orders = orderService.getOrdersByCashier(cashierId, start, end, search,pageable);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/today/branch/{branchId}")
    public ResponseEntity<List<OrderDTO>> getTodayOrders(@PathVariable Long branchId) {
        return ResponseEntity.ok(orderService.getTodayOrdersByBranch(branchId));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderDTO>> getCustomerOrders(@PathVariable Long customerId
    ) {
        return ResponseEntity.ok(orderService.getOrdersByCustomerId(customerId));
    }

    @GetMapping("/customer/t/{customerId}")
    public ResponseEntity<Page<OrderDTO>> getCustomerOrdersPagin(
            @PathVariable Long customerId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam(required = false) String search,
            Pageable pageable

    ) {
//        return ResponseEntity.ok(orderService.getOrdersByCustomerIdPagin(customerId, start, end, search,pageable));

        Page<OrderDTO> orders = orderService.getOrdersByCustomerIdPagin(customerId, start, end, search,pageable);
        return ResponseEntity.ok(orders);

    }

    @GetMapping("/recent/{branchId}")
    @PreAuthorize("hasAnyAuthority('ROLE_BRANCH_MANAGER', 'ROLE_BRANCH_ADMIN')")
    public ResponseEntity<List<OrderDTO>> getRecentOrders(@PathVariable Long branchId) {
        List<OrderDTO> recentOrders = orderService.getTop5RecentOrdersByBranchId(branchId);
        return ResponseEntity.ok(recentOrders);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_STORE_MANAGER') or hasAuthority('ROLE_STORE_ADMIN')")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }


}