package com.zosh.service;

import com.zosh.payload.dto.UpdateReturnDTO;
import com.zosh.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemRepository repository;

    public void updateReturnServices(Long id, UpdateReturnDTO dto) {

        repository.updateReturnDetails(
                id,
                dto.getReturned(),
                dto.getReturnReason(),
                dto.getReturnQuantity()
        );
    }
}
