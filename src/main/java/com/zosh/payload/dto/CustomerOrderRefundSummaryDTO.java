package com.zosh.payload.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerOrderRefundSummaryDTO {
    private CustomerDTO customer;
    private OrderDTO order;
    private Double totalRefundCash;
    private Double totalRefundCredit;
}
