package com.bednarmartin.budgetmanagementsystem.service.api.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAccountRequest {
    @NonNull
    private String name;
    @NonNull
    private BigDecimal initialBalance;
    @NonNull
    private String accountTypeName;
}
