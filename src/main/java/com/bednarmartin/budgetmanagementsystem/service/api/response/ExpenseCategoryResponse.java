package com.bednarmartin.budgetmanagementsystem.service.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseCategoryResponse {

    private Long id;

    private String name;

    private LocalDateTime dateCreated;
}
