package com.bednarmartin.budgetmanagementsystem.db.repository;

import com.bednarmartin.budgetmanagementsystem.db.model.Category;
import com.bednarmartin.budgetmanagementsystem.db.model.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);

    void deleteByName(String name);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Category SET name = :name, dateUpdated = :datetime, transactionType = :transaction_type  WHERE id = :id")
    void updateCategoryById(@Param("id") long id,
                            @Param("name") String name,
                            @Param("transaction_type") TransactionType type,
                            @Param("datetime") LocalDateTime dateTime);

}
