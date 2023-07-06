package com.bednarmartin.budgetmanagementsystem.service.api.request;

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

    private String accountTypeName;
}
