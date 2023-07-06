package com.bednarmartin.budgetmanagementsystem.service.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountTypeResponse {
    @NonNull
    private Long id;
    @NonNull
    private String name;
}
