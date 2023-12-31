package com.bednarmartin.budgetmanagementsystem.service.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTransactionRequest {

    @Positive(message = "The amount must be positive")
    @NotNull(message = "The amount is required!")
    private BigDecimal amount;

    @NotBlank(message = "The description is required!")
    @Size(min = 3, max = 300, message = "The length of the description must be between 3 and 300 characters!")
    private String description;

    @NotBlank(message = "The category name is required!")
    @Size(min = 3, max = 50, message = "The length of the category name must be between 3 and 50 characters!")
    private String categoryName;
}
