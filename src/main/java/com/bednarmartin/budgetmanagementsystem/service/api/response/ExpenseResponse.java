package com.bednarmartin.budgetmanagementsystem.service.api.response;

import com.bednarmartin.budgetmanagementsystem.db.model.ExpenseCategory;
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
public class ExpenseResponse {

    private Long id;

    private BigDecimal amount;

    private String description;

    private ExpenseCategory category;

    private LocalDateTime dateCreated;
}
