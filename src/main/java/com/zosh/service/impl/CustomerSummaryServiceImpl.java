package com.zosh.service.impl;

import com.zosh.mapper.CustomerMapper;
import com.zosh.payload.dto.CustomerDTO;
import com.zosh.payload.dto.CustomerFullSummaryDTO;
import com.zosh.repository.CustomerPaymentRepository;
import com.zosh.repository.OrderRepository;
import com.zosh.repository.RefundRepository;
import com.zosh.service.CustomerService;
import com.zosh.service.CustomerSummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerSummaryServiceImpl implements CustomerSummaryService {

    private final CustomerService customerService;
    private final OrderRepository orderRepository;
    private final RefundRepository refundRepository;
    private final CustomerPaymentRepository paymentRepository;

    @Override
    public CustomerFullSummaryDTO getCustomerFullSummary(Long customerId) {

        // --------------------------
        // 1) CUSTOMER
        // --------------------------
        CustomerDTO customer = CustomerMapper.toDto(customerService.getCustomerById(customerId));

        // --------------------------
        // 2) ORDER SUMMARY
        // --------------------------
        List<Object[]> orderSummaryList = orderRepository.getOrderSummary(customerId);
        Object[] orderSummary = orderSummaryList.isEmpty()
                ? new Object[]{0.0, 0.0, 0.0}
                : orderSummaryList.get(0);

        System.out.println("Order summary: " + Arrays.toString(orderSummary));

        double totalAmount = safeToDouble(orderSummary, 0);
        double totalCash   = safeToDouble(orderSummary, 1);
        double totalCredit = safeToDouble(orderSummary, 2);

        // --------------------------
        // 3) REFUND SUMMARY
        // --------------------------
        List<Object[]> refundSummaryList = refundRepository.getRefundSummary(customerId);
        Object[] refundSummary = refundSummaryList.isEmpty()
                ? new Object[]{0.0, 0.0, 0.0}
                : refundSummaryList.get(0);

        System.out.println("Refund summary: " + Arrays.toString(refundSummary));

        double refundCash   = safeToDouble(refundSummary, 0);
        double refundCredit = safeToDouble(refundSummary, 1);
        double refundTotal  = safeToDouble(refundSummary, 2);

        // --------------------------
        // 4) PAYMENT SUMMARY
        // --------------------------
        List<Object[]> paymentSummaryList = paymentRepository.getPaymentSummary(customerId);
        Object[] paymentSummary = paymentSummaryList.isEmpty()
                ? new Object[]{0.0, 0.0, 0.0}
                : paymentSummaryList.get(0);

        System.out.println("Payment summary: " + Arrays.toString(paymentSummary));

        double totalPaymentAmount = safeToDouble(paymentSummary, 0);
        double totalPaymentCash   = safeToDouble(paymentSummary, 1);
        double totalPaymentCredit = safeToDouble(paymentSummary, 2);

        // --------------------------
        // 5) RETURN DTO
        // --------------------------
        return CustomerFullSummaryDTO.builder()
                .customer(customer)

                .totalAmount(totalAmount)
                .totalCash(totalCash)
                .totalCredit(totalCredit)

                .totalRefundAmount(refundTotal)
                .totalRefundCash(refundCash)
                .totalRefundCredit(refundCredit)

                .totalPaymentAmount(totalPaymentAmount)
                .totalPaymentCash(totalPaymentCash)
                .totalPaymentCredit(totalPaymentCredit)
                .build();
    }

    /**
     * Safely converts an element of Object[] to double
     * Returns 0 if the array is null, element is null, or not a number
     */
    private double safeToDouble(Object[] array, int index) {
        if (array == null || array.length <= index || array[index] == null) return 0.0;
        if (array[index] instanceof Number) return ((Number) array[index]).doubleValue();
        return 0.0;
    }
}
