package com.zosh.payload.dto;

import lombok.Data;

@Data
public class UpdateReturnDTO {
    private Boolean returned;

    private String returnReason;
    private Integer returnQuantity;

}
