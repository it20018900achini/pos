package com.zosh.modal;

import lombok.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CustomerPaymentCreateDTO {

    @NotNull
    private Long customerId;

    @NotNull
    private Long cashierId;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal amount;

    @NotNull
    private String paymentMethod;

    private String reference;
    private String note;
}
