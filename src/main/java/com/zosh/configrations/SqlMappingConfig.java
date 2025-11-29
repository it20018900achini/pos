package com.zosh.configrations;
import com.zosh.payload.dto.BranchTransactionDTO;
import jakarta.persistence.SqlResultSetMapping;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.ColumnResult;
import org.springframework.context.annotation.Configuration;

@Configuration
@SqlResultSetMapping(
        name = "BranchTransactionMapping",
        classes = @ConstructorResult(
                targetClass = BranchTransactionDTO.class,
                columns = {
                        @ColumnResult(name = "type", type = String.class),
                        @ColumnResult(name = "id", type = Long.class),
                        @ColumnResult(name = "customer_name", type = String.class),
                        @ColumnResult(name = "cashier_name", type = String.class),
                        @ColumnResult(name = "amount", type = Double.class),
                        @ColumnResult(name = "payment_method", type = String.class),
                        @ColumnResult(name = "status", type = String.class),
                        @ColumnResult(name = "created_at", type = java.sql.Timestamp.class)
                }
        )
)
public class SqlMappingConfig {
}
