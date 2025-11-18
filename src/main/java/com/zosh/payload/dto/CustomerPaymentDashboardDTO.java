package com.zosh.payload.dto;

public class CustomerPaymentDashboardDTO {
    private PageResult<CustomerPaymentSummaryDTO> customerSummary;
    private PageResult<PaymentMethodSummaryDTO> paymentMethodSummary;

    public CustomerPaymentDashboardDTO(PageResult<CustomerPaymentSummaryDTO> customerSummary,
                                       PageResult<PaymentMethodSummaryDTO> paymentMethodSummary) {
        this.customerSummary = customerSummary;
        this.paymentMethodSummary = paymentMethodSummary;
    }

    // Getters and setters...
}
