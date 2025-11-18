package com.zosh.service.impl;

import com.zosh.domain.PaymentType;
import com.zosh.mapper.CustomerMapper;
import com.zosh.modal.PaymentMethod;
import com.zosh.payload.dto.*;
import com.zosh.service.CustomerService;
import com.zosh.service.CustomerSummaryService;
import com.zosh.service.OrderService;
import com.zosh.service.RefundService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerSummaryServiceImpl implements CustomerSummaryService {

    private final CustomerService customerService;
    private final OrderService orderService;
    private final RefundService refundService;
    private final CustomerSummaryService summaryService;

    @Override
    public CustomerFullSummaryDTO getCustomerFullSummary(Long customerId) {

        // 1️⃣ Fetch Customer entity and convert to DTO
        CustomerDTO customer = CustomerMapper.toDto(customerService.getCustomerById(customerId));

        // 2️⃣ Fetch all orders of this customer (already DTOs)
        List<OrderDTO> orders = orderService.getOrdersByCustomerId(customerId);

        // 3️⃣ Calculate totals
        double totalAmount = orders.stream()
                .mapToDouble(o -> o.getTotalAmount() != null ? o.getTotalAmount() : 0)
                .sum();

        double totalCash = orders.stream()
                .mapToDouble(o -> o.getCash() != null ? o.getCash() : 0)
                .sum();

        double totalCredit = orders.stream()
                .mapToDouble(o -> o.getCredit() != null ? o.getCredit() : 0)
                .sum();

        // 4️⃣ Fetch all refunds per order and calculate totals
        double totalRefundCash = 0;
        double totalRefundCredit = 0;

        for (OrderDTO order : orders) {
            List<RefundDTO> refunds = refundService.getRefundsByOrder(order.getId());
            totalRefundCash += refunds.stream()
                    .mapToDouble(r -> r.getCash() != null ? r.getCash() : 0)
                    .sum();
            totalRefundCredit += refunds.stream()
                    .mapToDouble(r -> r.getCredit() != null ? r.getCredit() : 0)
                    .sum();
        }

        double totalRefundAmount = totalRefundCash + totalRefundCredit;




        // 5️⃣ Build and return the summary DTO
        return CustomerFullSummaryDTO.builder()
                .customer(customer)
                .totalAmount(totalAmount)
                .totalCash(totalCash)
                .totalCredit(totalCredit)
                .totalRefundCash(totalRefundCash)
                .totalRefundCredit(totalRefundCredit)
                .totalRefundAmount(totalRefundAmount)
                .build();
    }
}
