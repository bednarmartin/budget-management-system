package com.bednarmartin.budgetmanagementsystem.service.api.request;

import com.bednarmartin.budgetmanagementsystem.db.model.enums.TransactionType;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {

    private BigDecimal amount;

    private String description;

    private String categoryName;

    private TransactionType type;

}
