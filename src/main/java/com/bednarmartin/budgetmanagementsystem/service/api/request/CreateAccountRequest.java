package com.bednarmartin.budgetmanagementsystem.service.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "The name is required!")
    @Size(min = 3, max = 50, message = "The length of the name must be between 3 and 50 characters!")
    private String name;

    @PositiveOrZero(message = "The initial balance must be positive or zero!")
    private BigDecimal initialBalance;

    @NotBlank(message = "The account type name is required!")
    @Size(min = 3, max = 50, message = "The length of the account type name must be between 3 and 50 characters!")
    private String accountTypeName;
}
