package com.zosh.specification;

import com.zosh.modal.CustomerPayment;
import com.zosh.modal.PaymentMethod;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class CustomerPaymentSpecifications {

    public static Specification<CustomerPayment> hasCustomerId(Long customerId) {
        return (root, query, builder) -> builder.equal(root.get("customer").get("id"), customerId);
    }

    public static Specification<CustomerPayment> hasCashierId(Long cashierId) {
        return (root, query, builder) -> builder.equal(root.get("cashier").get("id"), cashierId);
    }

    public static Specification<CustomerPayment> hasPaymentMethod(PaymentMethod method) {
        return (root, query, builder) -> builder.equal(root.get("paymentMethod"), method);
    }

    public static Specification<CustomerPayment> createdBetween(LocalDateTime start, LocalDateTime end) {
        return (root, query, builder) -> builder.between(root.get("createdAt"), start, end);
    }
}
