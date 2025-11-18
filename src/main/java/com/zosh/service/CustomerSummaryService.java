package com.zosh.service;

import com.zosh.payload.dto.CustomerFullSummaryDTO;

public interface CustomerSummaryService {
    CustomerFullSummaryDTO getCustomerFullSummary(Long customerId);
}
