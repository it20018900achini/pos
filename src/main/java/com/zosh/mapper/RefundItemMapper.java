package com.zosh.mapper;

import com.zosh.modal.RefundItem;
import com.zosh.payload.dto.RefundItemDTO;

public class RefundItemMapper {

    public static RefundItemDTO toDto(RefundItem item) {
        if (item == null) return null;

        return RefundItemDTO.builder()
                .id(item.getId())

                // ✅ Null-safe product mapping
                .productId(item.getProduct() != null ? item.getProduct().getId() : null)
                .product(item.getProduct() != null ? ProductMapper.toDto(item.getProduct()) : null)

                // ✅ Defensive defaults for possibly-null numeric/boolean fields
                .quantity(item.getQuantity() != null ? item.getQuantity() : 0)
                .returned(item.getReturned() != null ? item.getReturned() : false)
                .return_reason(item.getReturn_reason())
                .return_quantity(item.getReturn_quantity() != null ? item.getReturn_quantity() : 0)
                .price(item.getPrice() != null ? item.getPrice() : 0.0)

                .build();
    }
}
