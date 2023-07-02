package com.bednarmartin.budgetmanagementsystem.db.repository;

import com.bednarmartin.budgetmanagementsystem.db.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
}
