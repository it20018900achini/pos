package com.zosh.mapper;

import com.zosh.modal.Refund;
import com.zosh.payload.dto.RefundDTO;

import java.util.stream.Collectors;

public class RefundMapper {

    public static RefundDTO toDto(Refund refund) {
        return RefundDTO.builder()
                .id(refund.getId())
                .totalAmount(refund.getTotalAmount())
                .cash(refund.getCash())
                .credit(refund.getCredit())
                .branchId(refund.getBranch().getId())
                .cashierId(refund.getCashier().getId())
                .customer(refund.getCustomer())
                .createdAt(refund.getCreatedAt())
                .paymentType(refund.getPaymentType())
//                .status(refund.getStatus())
                .items(refund.getItems().stream()
                        .map(RefundItemMapper::toDto)
                        .collect(Collectors.toList()))
                .build();
    }
}
