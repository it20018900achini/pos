package com.zosh.controller;

import com.zosh.payload.dto.UpdateReturnDTO;
import com.zosh.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order-items")
@RequiredArgsConstructor
public class OrderItemController {

    private final OrderItemService service;

    @PatchMapping("/{id}/return")
    @PreAuthorize("hasAuthority('ROLE_CASHIER')")

    public ResponseEntity<?> updateReturnStatus(
            @PathVariable Long id,
            @RequestBody UpdateReturnDTO dto
    ) {
        service.updateReturnServices(id, dto);

        return ResponseEntity.ok("Return services updated");

    }
}
