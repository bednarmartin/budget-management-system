package com.bednarmartin.budgetmanagementsystem.db.repository;

import com.bednarmartin.budgetmanagementsystem.db.model.ExpenseCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExpenseCategoryRepository extends JpaRepository<ExpenseCategory, Long> {

    Optional<ExpenseCategory> findByName(String name);
}
