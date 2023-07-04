package com.bednarmartin.budgetmanagementsystem.service.api.response;

import com.bednarmartin.budgetmanagementsystem.db.model.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AccountResponse {

    private Long id;

    private String name;

    private BigDecimal balance;

    private AccountType accountType;
}
