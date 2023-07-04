package com.bednarmartin.budgetmanagementsystem.service.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountTypeResponse {
    private Long id;

    private String name;
}
