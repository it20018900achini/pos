package com.zosh.payload.dto;

public class CustomerPaymentSummaryDTO {
    private Long customerId;
    private Double totalAmount;

    public CustomerPaymentSummaryDTO(Long customerId, Double totalAmount) {
        this.customerId = customerId;
        this.totalAmount = totalAmount;
    }

    // Getters and setters...
}
