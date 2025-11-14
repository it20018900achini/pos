package com.zosh.mapper;

import com.zosh.modal.Refund;
import com.zosh.payload.dto.RefundDTO;

import java.util.Collections;
import java.util.stream.Collectors;

public class RefundMapper {

    public static RefundDTO toDto(Refund refund) {
        if (refund == null) return null;

        return RefundDTO.builder()
                .id(refund.getId())
                .totalAmount(refund.getTotalAmount())
                .cash(refund.getCash())
                .credit(refund.getCredit())

                // ✅ Null-safe relational mappings
                .branchId(refund.getBranch() != null ? refund.getBranch().getId() : null)
                .orderId(refund.getOrder() != null ? refund.getOrder().getId() : null)
                .cashierId(refund.getCashier() != null ? refund.getCashier().getId() : null)

                .customer(refund.getCustomer())
//                .order(refund.getOrder())
                .createdAt(refund.getCreatedAt())
                .paymentType(refund.getPaymentType())
                // .status(refund.getStatus()) // enable if needed

                // ✅ Null-safe items mapping
                .items(refund.getItems() != null
                        ? refund.getItems().stream()
                        .map(RefundItemMapper::toDto)
                        .collect(Collectors.toList())
                        : Collections.emptyList())

                .build();
    }
}
