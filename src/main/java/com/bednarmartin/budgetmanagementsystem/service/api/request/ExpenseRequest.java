package com.bednarmartin.budgetmanagementsystem.service.api.request;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseRequest {

    private BigDecimal amount;

    private String description;

    private String categoryName;

}
