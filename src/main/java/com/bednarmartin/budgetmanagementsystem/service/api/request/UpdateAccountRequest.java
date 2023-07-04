package com.bednarmartin.budgetmanagementsystem.service.api.request;

import com.bednarmartin.budgetmanagementsystem.db.model.AccountType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateAccountRequest {

    private String name;

    @ManyToOne
    @JoinColumn(name = "account_type_id", referencedColumnName = "id")
    private AccountType accountType;
}
