package com.bednarmartin.budgetmanagementsystem.controller.graphql;

import com.bednarmartin.budgetmanagementsystem.service.api.CategoryService;
import com.bednarmartin.budgetmanagementsystem.service.api.response.AmountSumByCategoryResponse;
import com.bednarmartin.budgetmanagementsystem.service.api.response.CategoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CategoryGraphqlController {

    private final CategoryService categoryService;

    @QueryMapping
    public CategoryResponse getCategoryById(@Argument Long id) {
        return categoryService.getCategoryById(id);
    }

    @QueryMapping
    public List<CategoryResponse> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @QueryMapping
    public List<AmountSumByCategoryResponse> getAllBalances() {
        return categoryService.getAmountSumByCategory();
    }

    @QueryMapping
    public AmountSumByCategoryResponse getBalanceByName(@Argument String categoryName) {
        return categoryService.getAmountSumByCategoryByCategoryName(categoryName);
    }
}
