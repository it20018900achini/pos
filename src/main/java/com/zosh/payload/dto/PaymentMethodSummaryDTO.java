package com.zosh.payload.dto;


import com.zosh.modal.PaymentMethod;

public class PaymentMethodSummaryDTO {
    private PaymentMethod paymentMethod;
    private Double totalAmount;

    public PaymentMethodSummaryDTO(PaymentMethod paymentMethod, Double totalAmount) {
        this.paymentMethod = paymentMethod;
        this.totalAmount = totalAmount;
    }

    // Getters and setters...
}
