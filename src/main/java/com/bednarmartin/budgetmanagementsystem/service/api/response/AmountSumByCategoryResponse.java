package com.bednarmartin.budgetmanagementsystem.service.api.response;

import lombok.*;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AmountSumByCategoryResponse {
    @NonNull
    String category;

    BigDecimal sum;
}
