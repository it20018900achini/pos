package com.zosh.repository;

import com.zosh.modal.Order;
import com.zosh.modal.Refund;
import com.zosh.modal.Refund;
import com.zosh.modal.User;
import com.zosh.payload.StoreAnalysis.BranchSalesDTO;
import com.zosh.payload.StoreAnalysis.PaymentInsightDTO;
import com.zosh.payload.StoreAnalysis.TimeSeriesPointDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RefundRepository extends JpaRepository<Refund, Long> {

    @Query("""
        SELECT 
            COALESCE(SUM(r.cash),0),
            COALESCE(SUM(r.credit),0),
            COALESCE(SUM(r.cash + r.credit),0)
        FROM Refund r
        WHERE r.order.customer.id = :customerId
    """)
    List<Object[]> getRefundSummary(@Param("customerId") Long customerId);
    // Existing paginated query
//    Page<Refund> findByCashierId(Long cashierId, Pageable pageable);
    List<Refund> findByOrderId(Long orderId);

    // New query: cashier + date range + search
    @Query("SELECT o FROM Refund o " +
            "WHERE o.cashier.id = :cashierId " +
            "AND (:start IS NULL OR o.createdAt >= :start) " +
            "AND (:end IS NULL OR o.createdAt <= :end) " +
            "AND (:search IS NULL OR CAST(o.id AS string) LIKE %:search% " +
            "OR LOWER(o.customer.fullName) LIKE %:search%)")
    Page<Refund> findByCashierId(
            @Param("cashierId") Long cashierId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("search") String search,
            Pageable pageable
    );
    // New query: cashier + date range + search
    @Query("SELECT o FROM Refund o " +
            "WHERE o.branch.id = :branchId " +
            "AND (:start IS NULL OR o.createdAt >= :start) " +
            "AND (:end IS NULL OR o.createdAt <= :end) " +
            "AND (" +
            "   :search IS NULL OR " +
            "   CAST(o.id AS string) LIKE %:search% OR " +
            "   LOWER(o.customer.fullName) LIKE %:search% OR " +
            "   LOWER(o.customer.phone) LIKE %:search% OR " +
            "   LOWER(o.customer.email) LIKE %:search%" +
            ")")
    Page<Refund> findByBranchIdPagin(
            @Param("branchId") Long branchId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("search") String search,
            Pageable pageable
    );

    @Query("""
       SELECT r 
       FROM Refund r 
       WHERE r.order.customer.id = :customerId
       """)
    List<Refund> findAllRefundsByCustomer(@Param("customerId") Long customerId);

    List<Refund> findByCustomerId(Long customerId);

    // New query: cashier + date range + search
    @Query("SELECT o FROM Refund o " +
            "WHERE o.customer.id = :customerId " +
            "AND (:start IS NULL OR o.createdAt >= :start) " +
            "AND (:end IS NULL OR o.createdAt <= :end) " +
            "AND (:search IS NULL OR CAST(o.id AS string) LIKE %:search% " +
            "OR LOWER(o.customer.fullName) LIKE %:search%)")
    Page<Refund> findByCustomerIdPagin(
            @Param("customerId") Long customerId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("search") String search,
            Pageable pageable
    );




    List<Refund> findByBranchId(Long branchId);
    List<Refund> findByBranchIdAndCreatedAtBetween(Long branchId,
                                                  LocalDateTime start,
                                                  LocalDateTime end);
    List<Refund> findByCashierAndCreatedAtBetween(User cashier,
                                                 LocalDateTime start,
                                                 LocalDateTime end);
//    List<Refund> findTop5ByBranchIdOrderByCreatedAtDesc(Long branchId);


    List<Refund> findTop5ByBranchIdOrderByCreatedAtDesc(Long branchId);

    @Query(""" 
            SELECT SUM(o.totalAmount) 
            FROM Refund o 
            WHERE o.branch.id = :branchId  
            AND o.createdAt BETWEEN :start AND :end
           """)
    Optional<BigDecimal> getTotalSalesBetween(@Param("branchId") Long branchId,
                                              @Param("start") LocalDateTime start,
                                              @Param("end") LocalDateTime end);

    @Query("""
        SELECT u.id, u.fullName, SUM(o.totalAmount) AS totalRevenue
        FROM Refund o
        JOIN o.cashier u
        WHERE o.branch.id = :branchId
        GROUP BY u.id, u.fullName
        ORDER BY totalRevenue DESC
    """)
    List<Object[]> getTopCashiersByRevenue(@Param("branchId") Long branchId);

    @Query("""
        SELECT COUNT(o)
        FROM Refund o
        WHERE o.branch.id = :branchId
        AND DATE(o.createdAt) = :date
    """)
    int countRefundsByBranchAndDate(@Param("branchId") Long branchId,
                                   @Param("date") LocalDate date);

    @Query("""
        SELECT COUNT(DISTINCT o.cashier.id)
        FROM Refund o
        WHERE o.branch.id = :branchId
        AND DATE(o.createdAt) = :date
    """)
    int countDistinctCashiersByBranchAndDate(@Param("branchId") Long branchId,
                                             @Param("date") LocalDate date);

    @Query("""
    SELECT o.paymentType, SUM(o.totalAmount), COUNT(o)
    FROM Refund o
    WHERE o.branch.id = :branchId
    AND DATE(o.createdAt) = :date
    GROUP BY o.paymentType
""")
    List<Object[]> getPaymentBreakdownByMethod(
            @Param("branchId") Long branchId,
            @Param("date") LocalDate date
    );

    ////////////////////


    @Query("SELECT SUM(o.totalAmount) FROM Refund o WHERE o.branch.store.storeAdmin.id = :storeAdminId")
    Optional<Double> sumTotalSalesByStoreAdmin(@Param("storeAdminId") Long storeAdminId);

    @Query("SELECT COUNT(o) FROM Refund o WHERE o.branch.store.storeAdmin.id = :storeAdminId")
    int countByStoreAdminId(@Param("storeAdminId") Long storeAdminId);
//

    @Query("""
    SELECT o FROM Refund o 
    WHERE o.branch.store.storeAdmin.id = :storeAdminId 
    AND o.createdAt BETWEEN :start AND :end
""")
    List<Refund> findAllByStoreAdminAndCreatedAtBetween(@Param("storeAdminId") Long storeAdminId,
                                                       @Param("start") LocalDateTime start,
                                                       @Param("end") LocalDateTime end);



    @Query("""
    SELECT new com.zosh.payload.StoreAnalysis.TimeSeriesPointDTO(
        o.createdAt,
        SUM(o.totalAmount)
    )
    FROM Refund o
    WHERE o.branch.store.storeAdmin.id = :storeAdminId
     AND o.createdAt BETWEEN :start AND :end
    GROUP BY o.createdAt
    ORDER BY o.createdAt
""")
    List<TimeSeriesPointDTO> getDailySales(@Param("storeAdminId") Long storeAdminId,
                                           @Param("start") LocalDateTime start,
                                           @Param("end") LocalDateTime end);


    @Query("""
        SELECT new com.zosh.payload.StoreAnalysis.PaymentInsightDTO(
            o.paymentType,
            SUM(o.totalAmount)
        )
        FROM Refund o
        WHERE o.branch.store.storeAdmin.id = :storeAdminId
        GROUP BY o.paymentType
    """)
    List<PaymentInsightDTO> getSalesByPaymentMethod(@Param("storeAdminId") Long storeAdminId);

    @Query("""
        SELECT new com.zosh.payload.StoreAnalysis.BranchSalesDTO(
            o.branch.name,
            SUM(o.totalAmount)
        )
        FROM Refund o
        WHERE o.branch.store.storeAdmin.id = :storeAdminId
        GROUP BY o.branch.id
    """)
    List<BranchSalesDTO> getSalesByBranch(@Param("storeAdminId") Long storeAdminId);





//    List<Refund> findByCustomerId(Long customerId);
}
