package com.bednarmartin.budgetmanagementsystem.service.api.response;

import com.bednarmartin.budgetmanagementsystem.db.model.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AmountSumByCategoryResponse {
    @NonNull
    Category category;

    BigDecimal sum;
}
