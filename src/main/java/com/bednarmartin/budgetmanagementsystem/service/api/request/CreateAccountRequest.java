package com.bednarmartin.budgetmanagementsystem.service.api.request;

import com.bednarmartin.budgetmanagementsystem.db.model.AccountType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAccountRequest {

    private String name;

    private BigDecimal initialBalance;

    @ManyToOne
    @JoinColumn(name = "account_type_id", referencedColumnName = "id")
    private AccountType accountType;
}
