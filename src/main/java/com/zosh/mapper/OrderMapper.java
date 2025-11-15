package com.zosh.mapper;

import com.zosh.modal.Order;
import com.zosh.payload.dto.OrderDTO;

import java.util.Collections;
import java.util.stream.Collectors;

public class OrderMapper {

    public static OrderDTO toDto(Order order) {
        if (order == null) return null;

        return OrderDTO.builder()
                .id(order.getId())
                .orderId(order.getId()) // optional, if you need a separate field
                .totalAmount(order.getTotalAmount())
                .cash(order.getCash())
                .credit(order.getCredit())

                // Null-safe relational IDs
                .branchId(order.getBranch() != null ? order.getBranch().getId() : null)
                .cashierId(order.getCashier() != null ? order.getCashier().getId() : null)

                // Null-safe customer mapping (if you want to map full DTO, you can use CustomerMapper)
                .customer(order.getCustomer())

                .createdAt(order.getCreatedAt())
                .paymentType(order.getPaymentType())
                .status(order.getStatus())

                // Null-safe items mapping
                .items(order.getItems() != null
                        ? order.getItems().stream()
                        .map(OrderItemMapper::toDto)
                        .collect(Collectors.toList())
                        : Collections.emptyList())
                .build();
    }
}
