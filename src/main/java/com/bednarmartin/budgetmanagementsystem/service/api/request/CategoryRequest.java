package com.bednarmartin.budgetmanagementsystem.service.api.request;

import com.bednarmartin.budgetmanagementsystem.db.model.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequest {

    private String name;

    private TransactionType transactionType;
}
