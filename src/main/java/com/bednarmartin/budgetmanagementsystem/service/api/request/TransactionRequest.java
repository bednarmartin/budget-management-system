package com.bednarmartin.budgetmanagementsystem.service.api.request;

import com.bednarmartin.budgetmanagementsystem.db.model.enums.TransactionType;
import lombok.*;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {
    @NonNull
    private BigDecimal amount;
    @NonNull
    private String description;
    @NonNull
    private String categoryName;
    @NonNull
    private TransactionType type;
    @NonNull
    private String accountName;

}
