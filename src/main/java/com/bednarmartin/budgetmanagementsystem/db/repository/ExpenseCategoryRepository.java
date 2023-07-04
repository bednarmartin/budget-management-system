package com.bednarmartin.budgetmanagementsystem.db.repository;

import com.bednarmartin.budgetmanagementsystem.db.model.ExpenseCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ExpenseCategoryRepository extends JpaRepository<ExpenseCategory, Long> {
    Optional<ExpenseCategory> findByName(String name);

    void deleteByName(String name);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE ExpenseCategory SET name = :name, dateUpdated = :datetime WHERE id = :id")
    void updateExpenseCategoryById(@Param("id") long id,
                                   @Param("name") String name,
                                   @Param("datetime") LocalDateTime dateTime);

}
