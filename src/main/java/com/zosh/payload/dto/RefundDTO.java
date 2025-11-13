package com.zosh.payload.dto;

import com.zosh.domain.RefundStatus;
import com.zosh.domain.PaymentType;
import com.zosh.modal.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefundDTO {

    private Long id;
    private Double totalAmount;
    private Double cash;
    private Double credit;
    private Long branchId;
    private Long orderId;
    private Long cashierId;
    private Customer customer;
    private List<RefundItemDTO> items;
    private LocalDateTime createdAt;
    private PaymentType paymentType;
//    private RefundStatus status;
}
