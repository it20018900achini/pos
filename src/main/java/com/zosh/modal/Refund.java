package com.zosh.modal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zosh.domain.PaymentType;
import com.zosh.domain.RefundStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "refunds")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Refund {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double totalAmount;
    private Double cash;
    private Double credit;
    private Double discount;

    private LocalDateTime createdAt;

    @ManyToOne
    @JsonIgnore
    private Branch branch;

//    @ManyToOne
//    @JsonIgnore
//    private Order order;

    @ManyToOne
    @JoinColumn
    private Order order;


    @ManyToOne
    @JsonIgnore
    private User cashier;

    @ManyToOne
    private Customer customer;

    private PaymentType paymentType;

    @OneToMany(mappedBy = "refund", cascade = CascadeType.ALL)
    private List<RefundItem> items;

//    private RefundStatus status=RefundStatus.COMPLETED;


    // 👇 ADD THIS RELATIONSHIP
    @ManyToOne
    @JoinColumn(name = "shift_report_id")
    private ShiftReport shiftReport;


    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
    }
}

