package com.zosh.repository;
import com.zosh.payload.dto.BranchTransactionDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class BranchTransactionRepositoryImpl implements BranchTransactionRepositoryCustom {

    private final EntityManager em;

    public BranchTransactionRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<BranchTransactionDTO> findMergedTransactions(List<Long> branchIds,
                                                             LocalDateTime startDate,
                                                             LocalDateTime endDate,
                                                             String paymentType,
                                                             int page,
                                                             int size) {

        String sql = """
            SELECT * FROM (
                -- Orders
                SELECT 'ORDER' AS type, o.id, c.full_name AS customer_name, u.full_name AS cashier_name,
                       o.total_amount AS amount, o.payment_type AS payment_method,
                       o.status AS status, o.created_at
                FROM orders o
                LEFT JOIN customer c ON o.customer_id = c.id
                LEFT JOIN users u ON o.cashier_id = u.id
                WHERE o.branch_id IN :branchIds
                  AND (:startDate IS NULL OR o.created_at >= :startDate)
                  AND (:endDate IS NULL OR o.created_at <= :endDate)
                  AND (:paymentType IS NULL OR o.payment_type = :paymentType)

                UNION ALL

                -- Refunds
                SELECT 'REFUND' AS type, r.id, c.full_name, u.full_name, r.total_amount,
                       r.payment_type, NULL AS status, r.created_at
                FROM refunds r
                LEFT JOIN customer c ON r.customer_id = c.id
                LEFT JOIN users u ON r.cashier_id = u.id
                WHERE r.branch_id IN :branchIds
                  AND (:startDate IS NULL OR r.created_at >= :startDate)
                  AND (:endDate IS NULL OR r.created_at <= :endDate)
                  AND (:paymentType IS NULL OR r.payment_type = :paymentType)

                UNION ALL

                -- Customer Payments
                SELECT 'CUSTOMER_PAYMENT' AS type, cp.id, c.full_name, u.full_name, cp.amount,
                       cp.payment_method, NULL AS status, cp.created_at
                FROM customer_payment cp
                LEFT JOIN customer c ON cp.customer_id = c.id
                LEFT JOIN users u ON cp.cashier_id = u.id
                WHERE cp.branch_id IN :branchIds
                  AND (:startDate IS NULL OR cp.created_at >= :startDate)
                  AND (:endDate IS NULL OR cp.created_at <= :endDate)
                  AND (:paymentType IS NULL OR cp.payment_method = :paymentType)
            ) AS combined
            ORDER BY created_at ASC
            """;

        if (page >= 0 && size > 0) {
            sql += " LIMIT :limit OFFSET :offset";
        }

        Query query = em.createNativeQuery(sql);

        query.setParameter("branchIds", branchIds);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        query.setParameter("paymentType", paymentType);

        if (page >= 0 && size > 0) {
            query.setParameter("limit", size);
            query.setParameter("offset", page * size);
        }

        List<Object[]> results = query.getResultList();

        // Map each row manually to DTO, safely converting enum/byte columns to String
        return results.stream().map(row -> new BranchTransactionDTO(
                row[0] != null ? row[0].toString() : null,                 // type
                ((Number) row[1]).longValue(),                             // id
                row[2] != null ? row[2].toString() : null,                 // customerName
                row[3] != null ? row[3].toString() : null,                 // cashierName
                row[4] != null ? ((Number) row[4]).doubleValue() : null,   // amount
                row[5] != null ? row[5].toString() : null,                 // paymentMethod
                row[6] != null ? row[6].toString() : null,                 // status
                row[7] != null ? ((java.sql.Timestamp) row[7]).toLocalDateTime() : null // createdAt
        )).collect(Collectors.toList());
    }
}
