package com.zosh.service;


import com.zosh.domain.OrderStatus;
import com.zosh.domain.PaymentType;
import com.zosh.domain.RefundStatus;
import com.zosh.exception.UserException;
import com.zosh.payload.dto.OrderDTO;

import com.zosh.payload.dto.RefundDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderDTO createOrder(OrderDTO dto) throws UserException;
    OrderDTO getOrderById(Long id);

    List<OrderDTO> getOrdersByBranch(Long branchId,
                                     Long customerId,
                                     Long cashierId,
                                     PaymentType paymentType,
                                     OrderStatus status);

    // ← CHANGE THIS
    Page<OrderDTO> getOrdersByCashier(
            Long cashierId,
            LocalDateTime start,
            LocalDateTime end,
            String search,
            Pageable pageable
    );

    void deleteOrder(Long id);
    List<OrderDTO> getTodayOrdersByBranch(Long branchId);
    List<OrderDTO> getOrdersByCustomerId(Long customerId);
    Page<OrderDTO> getOrdersByCustomerIdPagin(
            Long customerId,
            LocalDateTime start,
            LocalDateTime end,
            String search,
            Pageable pageable
    );


    Page<OrderDTO> getOrdersByBranchPagin(  Long branchId,
                                            Long customerId,
                                            Long cashierId,
                                            PaymentType paymentType,
                                            OrderStatus status,

                                            LocalDateTime start,
                                            LocalDateTime end,
                                            String search,
                                            Pageable pageable
    );
    List<OrderDTO> getTop5RecentOrdersByBranchId(Long branchId);
}