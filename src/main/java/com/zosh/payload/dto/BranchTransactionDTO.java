package com.zosh.payload.dto;import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BranchTransactionDTO {

    private String type;           // "ORDER", "REFUND", "CUSTOMER_PAYMENT"
    private Long id;               // Transaction ID
    private String customerName;   // Customer full name
    private String cashierName;    // Cashier username
    private Double amount;         // Total amount / payment amount
    private String paymentMethod;  // Payment type/method
    private String status;         // Order/Refund status (nullable for CustomerPayment)
    private LocalDateTime createdAt; // Created timestamp
}
