package com.bednarmartin.budgetmanagementsystem.service.api.response;

import com.bednarmartin.budgetmanagementsystem.db.model.Account;
import com.bednarmartin.budgetmanagementsystem.db.model.Category;
import com.bednarmartin.budgetmanagementsystem.db.model.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponse {
    @NonNull
    private Long id;
    @NonNull
    private BigDecimal amount;
    @NonNull
    private String description;
    @NonNull
    private Category category;
    @NonNull
    private TransactionType type;
    @NonNull
    private LocalDateTime dateCreated;
    @NonNull
    private Account account;
}
