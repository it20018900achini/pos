package com.zosh.service.impl;

import com.zosh.mapper.CustomerMapper;
import com.zosh.payload.dto.*;
import com.zosh.service.CustomerPaymentService;
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
    private final CustomerPaymentService customerPaymentService;

    @Override
    public CustomerFullSummaryDTO getCustomerFullSummary(Long customerId) {

        // 1️⃣ Customer info
        CustomerDTO customer = CustomerMapper.toDto(customerService.getCustomerById(customerId));

        // 2️⃣ Orders
        List<OrderDTO> orders = orderService.getOrdersByCustomerId(customerId);
        double totalAmount = orders.stream().mapToDouble(o -> o.getTotalAmount() != null ? o.getTotalAmount() : 0).sum();
        double totalCash = orders.stream().mapToDouble(o -> o.getCash() != null ? o.getCash() : 0).sum();
        double totalCredit = orders.stream().mapToDouble(o -> o.getCredit() != null ? o.getCredit() : 0).sum();

        // 3️⃣ Refunds
        double totalRefundCash = 0;
        double totalRefundCredit = 0;
        for (OrderDTO order : orders) {
            List<RefundDTO> refunds = refundService.getRefundsByOrder(order.getId());
            totalRefundCash += refunds.stream().mapToDouble(r -> r.getCash() != null ? r.getCash() : 0).sum();
            totalRefundCredit += refunds.stream().mapToDouble(r -> r.getCredit() != null ? r.getCredit() : 0).sum();
        }
        double totalRefundAmount = totalRefundCash + totalRefundCredit;

        // 4️⃣ Customer Payments
//        List<CustomerPaymentDTO> payments = customerPaymentService.getPaymentsByCustomerId(customerId);
//        double totalPaymentAmount = payments.stream().mapToDouble(p -> p.getAmount() != null ? p.getAmount() : 0).sum();
//        double totalPaymentCash = payments.stream()
//                .filter(p -> p.getPaymentMethod() == com.zosh.domain.PaymentType.CASH)
//                .mapToDouble(p -> p.getAmount() != null ? p.getAmount() : 0)
//                .sum();
//        double totalPaymentCredit = payments.stream()
//                .filter(p -> p.getPaymentMethod() == com.zosh.domain.PaymentType.CARD)
//                .mapToDouble(p -> p.getAmount() != null ? p.getAmount() : 0)
//                .sum();

        // 5️⃣ Build DTO
        return CustomerFullSummaryDTO.builder()
                .customer(customer)
                .totalAmount(totalAmount)
                .totalCash(totalCash)
                .totalCredit(totalCredit)
                .totalRefundCash(totalRefundCash)
                .totalRefundCredit(totalRefundCredit)
                .totalRefundAmount(totalRefundAmount)
//                .totalPaymentAmount(totalPaymentAmount)
//                .totalPaymentCash(totalPaymentCash)
//                .totalPaymentCredit(totalPaymentCredit)
                .build();
    }
}
