package com.bednarmartin.budgetmanagementsystem.service.api.response;

import com.bednarmartin.budgetmanagementsystem.db.model.Category;
import com.bednarmartin.budgetmanagementsystem.db.model.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponse {

    private Long id;

    private BigDecimal amount;

    private String description;

    private Category category;

    private TransactionType type;

    private LocalDateTime dateCreated;
}
