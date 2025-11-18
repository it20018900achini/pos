package com.zosh.mapper;

import com.zosh.modal.CustomerPayment;
import com.zosh.payload.dto.CustomerPaymentDTO;

public class CustomerPaymentMapper {

    public static CustomerPaymentDTO toDto(CustomerPayment entity) {
        if (entity == null) return null;

        return CustomerPaymentDTO.builder()
                .id(entity.getId())
                .customerId(entity.getCustomer() != null ? entity.getCustomer().getId() : null)
                .cashierId(entity.getCashier() != null ? entity.getCashier().getId() : null)
                .amount(entity.getAmount())
                .paymentMethod(entity.getPaymentMethod())
                .reference(entity.getReference())
                .note(entity.getNote())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public static CustomerPayment toEntity(CustomerPaymentDTO dto) {
        if (dto == null) return null;

        CustomerPayment entity = new CustomerPayment();

        entity.setId(dto.getId());
        entity.setAmount(dto.getAmount());
        entity.setPaymentMethod(dto.getPaymentMethod());
        entity.setReference(dto.getReference());
        entity.setNote(dto.getNote());

        // DO NOT set Customer or Cashier here — service layer handles it
        return entity;
    }
}
