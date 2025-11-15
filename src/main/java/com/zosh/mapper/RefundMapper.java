package com.zosh.mapper;

import com.zosh.modal.Refund;
import com.zosh.payload.dto.RefundDTO;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RefundMapper {

    // Single Refund → RefundDTO
    public static RefundDTO toDto(Refund refund) {
        if (refund == null) return null;

        return RefundDTO.builder()
                .id(refund.getId())
                .totalAmount(refund.getTotalAmount())
                .cash(refund.getCash())
                .credit(refund.getCredit())

                // Relational IDs
                .branchId(refund.getBranch() != null ? refund.getBranch().getId() : null)
                .cashierId(refund.getCashier() != null ? refund.getCashier().getId() : null)
                .customer(refund.getCustomer())

                // FULL Order details
                .order(refund.getOrder() != null ? OrderMapper.toDto(refund.getOrder()) : null)

                .createdAt(refund.getCreatedAt())
                .paymentType(refund.getPaymentType())

                // Null-safe items mapping
                .items(refund.getItems() != null
                        ? refund.getItems().stream()
                        .map(RefundItemMapper::toDto)
                        .collect(Collectors.toList())
                        : Collections.emptyList())
                .build();
    }

    // Map list of Refunds
    public static List<RefundDTO> toDtoList(List<Refund> refunds) {
        if (refunds == null) return Collections.emptyList();
        return refunds.stream()
                .map(RefundMapper::toDto)
                .collect(Collectors.toList());
    }
}
