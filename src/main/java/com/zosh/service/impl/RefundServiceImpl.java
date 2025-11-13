package com.zosh.service.impl;


import com.zosh.domain.RefundStatus;
import com.zosh.domain.PaymentType;
import com.zosh.exception.UserException;
import com.zosh.mapper.RefundMapper;
import com.zosh.modal.*;
import com.zosh.payload.dto.RefundDTO;
import com.zosh.repository.*;

import com.zosh.service.RefundService;
import com.zosh.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
public class RefundServiceImpl implements RefundService {

    private final RefundRepository refundRepository;
    private final ProductRepository productRepository;
    private final BranchRepository branchRepository;
    private final OrderRepository OrderRepository;
    private final UserService userService;

    @Override
    public RefundDTO createRefund(RefundDTO dto) throws UserException {
//        System.out.println(dto.getOrderId());
        User cashier = userService.getCurrentUser();

        Branch branch=cashier.getBranch();

        if(branch==null){
            throw new UserException("cashier's branch is null");
        }

        Refund refund = Refund.builder()
                .branch(branch)
                .cashier(cashier)
                .customer(dto.getCustomer())
                .paymentType(dto.getPaymentType())
                .build();


        // ✅ Set the Order entity
        if (dto.getOrderId() != null) {
            Order order = OrderRepository.findById(dto.getOrderId())
                    .orElseThrow(() -> new UserException("Order not found with ID " + dto.getOrderId()));
            refund.setOrder(order);
        }


        List<RefundItem> refundItems = dto.getItems().stream().map(itemDto -> {
            Product product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found"));

            return RefundItem.builder()
                    .product(product)
                    .quantity(itemDto.getQuantity())
                    .price(product.getSellingPrice() * itemDto.getQuantity())
                    .refund(refund)

                    .build();
        }).toList();

        double total = refundItems.stream().mapToDouble(RefundItem::getPrice).sum();
//        double cash = refund.getCash();
//        double credit = refund.getCredit();
        refund.setTotalAmount(total);
        refund.setCash(dto.getCash());
        refund.setCredit(dto.getCredit());
        System.out.println(dto.getCredit());
        refund.setItems(refundItems);
        return RefundMapper.toDto(refundRepository.save(refund));
    }

    @Override
    public RefundDTO getRefundById(Long id) {
        return refundRepository.findById(id)
                .map(RefundMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Refund not found"));
    }



    @Override
    public List<RefundDTO> getRefundsByBranch(Long branchId,
                                            Long customerId,
                                            Long cashierId,
                                            PaymentType paymentType,
                                            RefundStatus status) {
        return refundRepository.findByBranchId(branchId).stream()

                // ✅ Filter by Customer ID (if provided)
                .filter(refund -> customerId == null ||
                        (refund.getCustomer() != null &&
                                refund.getCustomer().getId().equals(customerId)))

                // ✅ Filter by Cashier ID (if provided)
                .filter(refund -> cashierId==null ||
                        (refund.getCashier() != null &&
                                refund.getCashier().getId().equals(cashierId)))

                // ✅ Filter by Payment Type (if provided)
                .filter(refund -> paymentType == null ||
                        refund.getPaymentType() == paymentType)

                // ✅ Filter by Status (if provided)
//                .filter(refund -> status() == null ||
//                        refund.getStatus() == status)

                // ✅ Map to DTO
                .map(RefundMapper::toDto)

                // ✅ Sort by createdAt (latest first)
                .sorted((o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt()))

                .collect(Collectors.toList());
//        return refundRepository.findByBranchId(branchId).stream()
//                .map(RefundMapper::toDto)
//                .collect(Collectors.toList());
    }
    @Override
    public Page<RefundDTO> getRefundsByCashier(Long cashierId,
                                             LocalDateTime start, LocalDateTime end, String search,Pageable pageable) {
        Page<Refund> refunds = refundRepository.findByCashierId(cashierId, start, end, search, pageable);
        return refunds.map(RefundMapper::toDto);
    }



    @Override
    public void deleteRefund(Long id) {
        if (!refundRepository.existsById(id)) {
            throw new EntityNotFoundException("Refund not found");
        }
        refundRepository.deleteById(id);
    }

    @Override
    public List<RefundDTO> getTodayRefundsByBranch(Long branchId) {
        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.plusDays(1).atStartOfDay();

        return refundRepository.findByBranchIdAndCreatedAtBetween(branchId, start, end)
                .stream()
                .map(RefundMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RefundDTO> getRefundsByCustomerId(Long customerId) {
        List<Refund> refunds = refundRepository.findByCustomerId(customerId);

        return refunds.stream()
                .map(RefundMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RefundDTO> getTop5RecentRefundsByBranchId(Long branchId) {
        branchRepository.findById(branchId)
                .orElseThrow(() -> new EntityNotFoundException("Branch not found with ID: " + branchId));

        List<Refund> refunds = refundRepository.findTop5ByBranchIdOrderByCreatedAtDesc(branchId);
        return refunds.stream()
                .map(RefundMapper::toDto)
                .collect(Collectors.toList());
    }

}
