package com.zosh.payload.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerFullSummaryDTO {
    private CustomerDTO customer;

    // Totals
    private Double totalAmount;          // sum of all orders
    private Double totalCash;            // sum of all order cash
    private Double totalCredit;          // sum of all order credit
    private Double totalRefundAmount;    // sum of all refunds (cash + credit)
    private Double totalRefundCash;      // sum of cash refunds
    private Double totalRefundCredit;    // sum of credit refunds
   ;

}
