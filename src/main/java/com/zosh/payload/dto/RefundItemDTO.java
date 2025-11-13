package com.zosh.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefundItemDTO {

    private Long id;
    private Long productId;
    private Integer quantity;
    private ProductDTO product;
    private Double price;
    private Boolean returned;
    private String return_reason;
    private Integer return_quantity;


}
