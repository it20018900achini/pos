package com.zosh.service;


import com.zosh.domain.OrderStatus;
import com.zosh.domain.PaymentType;
import com.zosh.exception.UserException;
import com.zosh.payload.dto.OrderDTO;

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
    Page<OrderDTO> getOrdersByCashier(Long cashierId, Pageable pageable,
                                      LocalDateTime start, LocalDateTime end, String search);

    void deleteOrder(Long id);
    List<OrderDTO> getTodayOrdersByBranch(Long branchId);
    List<OrderDTO> getOrdersByCustomerId(Long customerId);
    List<OrderDTO> getTop5RecentOrdersByBranchId(Long branchId);
}