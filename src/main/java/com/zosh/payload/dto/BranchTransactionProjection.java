package com.zosh.payload.dto;
import java.time.LocalDateTime;

public interface BranchTransactionProjection {
    String getType();           // ORDER, REFUND, CUSTOMER_PAYMENT
    Long getId();
    String getCustomerName();
    String getCashierName();
    Double getAmount();
    String getPaymentMethod();
    String getStatus();
    LocalDateTime getCreatedAt();
}
