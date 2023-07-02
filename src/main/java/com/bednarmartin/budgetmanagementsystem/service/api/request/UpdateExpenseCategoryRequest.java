package com.bednarmartin.budgetmanagementsystem.service.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateExpenseCategoryRequest extends CreateExpenseCategoryRequest {

    private LocalDateTime dateCreated;
}
