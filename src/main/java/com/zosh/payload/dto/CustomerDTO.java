package com.zosh.payload.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerDTO {
    private Long id;
    private String fullName;
    private String email;
    private String phone;
}