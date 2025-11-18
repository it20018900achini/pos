package com.zosh.payload.dto;

import com.zosh.modal.PaymentMethod;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerPaymentDTO {
    private Long id;
    private Long customerId;
    private Long cashierId;
    private Long branchId;
    private Double amount;
    private PaymentMethod paymentMethod;
    private String reference;
    private String note;
    private LocalDateTime createdAt;
}
