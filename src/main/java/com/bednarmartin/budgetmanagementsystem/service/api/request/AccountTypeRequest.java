package com.bednarmartin.budgetmanagementsystem.service.api.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountTypeRequest {
    @NonNull
    private String name;
}
