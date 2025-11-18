package com.zosh.service;

import com.zosh.payload.dto.CustomerFullSummaryDTO;
import com.zosh.payload.dto.CustomerPaymentSummaryDTO;

public interface CustomerSummaryService {

    CustomerFullSummaryDTO getCustomerFullSummary(Long customerId);
}
