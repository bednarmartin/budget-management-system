package com.bednarmartin.budgetmanagementsystem.db.repository;

import com.bednarmartin.budgetmanagementsystem.db.model.Expense;
import com.bednarmartin.budgetmanagementsystem.db.model.ExpenseCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    @Modifying
    @Query("""
            UPDATE Expense SET amount = :amount, description = :description, category = :category,
            dateUpdated = :datetime WHERE id = :id
            """)
    void updateExpenseCategoryById(@Param("id") long id,
                                   @Param("amount") BigDecimal amount,
                                   @Param("description") String description,
                                   @Param("category") ExpenseCategory category,
                                   @Param("datetime") LocalDateTime dateTime);
}
