package com.zosh.service;

import com.zosh.payload.dto.CustomerFullSummaryDTO;
import com.zosh.payload.dto.CustomerPaymentDTO;

import java.util.List;

public interface CustomerSummaryService {
    CustomerFullSummaryDTO getCustomerFullSummary(Long customerId);

}
