package com.zosh.service.impl;

import com.zosh.mapper.CustomerMapper;
import com.zosh.payload.dto.*;
import com.zosh.modal.PaymentMethod;
import com.zosh.service.CustomerService;
import com.zosh.service.CustomerSummaryService;
import com.zosh.service.CustomerPaymentService;
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

        // Customer
        CustomerDTO customer = CustomerMapper.toDto(customerService.getCustomerById(customerId));

        // Orders
        List<OrderDTO> orders = orderService.getOrdersByCustomerId(customerId);

        double totalAmount = orders.stream()
                .mapToDouble(o -> o.getTotalAmount() != null ? o.getTotalAmount() : 0)
                .sum();

        double totalCash = orders.stream()
                .mapToDouble(o -> o.getCash() != null ? o.getCash() : 0)
                .sum();

        double totalCredit = orders.stream()
                .mapToDouble(o -> o.getCredit() != null ? o.getCredit() : 0)
                .sum();

        // Refunds
        double refundCash = 0;
        double refundCredit = 0;

        for (OrderDTO order : orders) {
            List<RefundDTO> refunds = refundService.getRefundsByOrder(order.getId());

            refundCash += refunds.stream()
                    .mapToDouble(r -> r.getCash() != null ? r.getCash() : 0)
                    .sum();

            refundCredit += refunds.stream()
                    .mapToDouble(r -> r.getCredit() != null ? r.getCredit() : 0)
                    .sum();
        }

        double refundAmount = refundCash + refundCredit;

        // Payments
        List<CustomerPaymentDTO> payments = customerPaymentService.getPaymentsByCustomerId(customerId);

        double totalPaymentAmount = payments.stream()
                .mapToDouble(p -> p.getAmount() != null ? p.getAmount() : 0)
                .sum();

        double totalPaymentCash = payments.stream()
                .filter(p -> p.getPaymentMethod() == PaymentMethod.CASH)
                .mapToDouble(p -> p.getAmount() != null ? p.getAmount() : 0)
                .sum();

        double totalPaymentCredit = payments.stream()
                .filter(p -> p.getPaymentMethod() == PaymentMethod.CARD)
                .mapToDouble(p -> p.getAmount() != null ? p.getAmount() : 0)
                .sum();

        return CustomerFullSummaryDTO.builder()
                .customer(customer)
                .totalAmount(totalAmount)
                .totalCash(totalCash)
                .totalCredit(totalCredit)

                .totalRefundAmount(refundAmount)
                .totalRefundCash(refundCash)
                .totalRefundCredit(refundCredit)

                .totalPaymentAmount(totalPaymentAmount)
                .totalPaymentCash(totalPaymentCash)
                .totalPaymentCredit(totalPaymentCredit)

                .build();
    }
}
