package com.bednarmartin.budgetmanagementsystem.service.api.response;

import com.bednarmartin.budgetmanagementsystem.db.model.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse {

    private Long id;

    private String name;

    private TransactionType transactionType;

    private LocalDateTime dateCreated;
}
