package com.bednarmartin.budgetmanagementsystem.service.api.request;

import lombok.*;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateExpenseRequest extends CreateExpenseRequest {

    private LocalDateTime dateCreated;
}
