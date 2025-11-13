package com.zosh.mapper;

import com.zosh.modal.RefundItem;
import com.zosh.payload.dto.RefundItemDTO;

public class RefundItemMapper {

    public static RefundItemDTO toDto(RefundItem item) {
        if (item == null) return null;

        return RefundItemDTO.builder()
                .id(item.getId())
                .productId(item.getProduct() != null ? item.getProduct().getId() : null)
                .quantity(item.getQuantity())
                .returned(item.getReturned())
                .return_reason(item.getReturn_reason())
                .return_quantity(item.getReturn_quantity())
                .price(item.getPrice())
                .product(item.getProduct() != null ? ProductMapper.toDto(item.getProduct()) : null)
                .build();
    }
}
