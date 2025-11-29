package com.zosh.payload.dto;

import com.zosh.modal.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerFullSummaryDTO {

    // -------------------
    // Customer info
    // -------------------
    private CustomerDTO customer;

    // -------------------
    // Orders summary
    // -------------------
    private double totalAmount;
    private double totalCash;
    private double totalCredit;

    // -------------------
    // Refunds summary
    // -------------------
    private double totalRefundAmount;
    private double totalRefundCash;
    private double totalRefundCredit;

    // -------------------
    // Payments summary
    // -------------------
    private double totalPaymentAmount;
    private double totalPaymentCash;
    private double totalPaymentCredit;
}
