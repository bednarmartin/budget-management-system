package com.bednarmartin.budgetmanagementsystem.service.api.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateAccountRequest {
    @NonNull
    private String name;
    @NonNull
    private String accountTypeName;
}
