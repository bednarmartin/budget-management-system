package com.bednarmartin.budgetmanagementsystem.db.repository;

import com.bednarmartin.budgetmanagementsystem.annotations.LogMethod;
import com.bednarmartin.budgetmanagementsystem.db.model.Category;
import com.bednarmartin.budgetmanagementsystem.db.model.enums.TransactionType;
import com.bednarmartin.budgetmanagementsystem.service.api.response.AmountSumByCategoryResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @LogMethod
    Optional<Category> findByName(String name);

    @LogMethod
    void deleteByName(String name);

    @LogMethod
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Category SET name = :name, dateUpdated = :datetime, transactionType = :transaction_type  WHERE id = :id")
    void updateCategoryById(@Param("id") long id,
                            @Param("name") String name,
                            @Param("transaction_type") TransactionType type,
                            @Param("datetime") LocalDateTime dateTime);

    @LogMethod
    @Query("""
            SELECT
            new com.bednarmartin.budgetmanagementsystem.service.api.response.AmountSumByCategoryResponse(c, SUM(t.amount))
            FROM Category c
            LEFT OUTER JOIN Transaction t ON c.id = t.category.id
            GROUP BY c.name
            """)
    List<AmountSumByCategoryResponse> getAllAmountSumsByCategory();

    @LogMethod
    @Query("""
            SELECT
            new com.bednarmartin.budgetmanagementsystem.service.api.response.AmountSumByCategoryResponse(c, SUM(t.amount))
            FROM Category c
            LEFT OUTER JOIN Transaction t ON c.id = t.category.id
            WHERE c.name = :name
            GROUP BY c.name
            """)
    Optional<AmountSumByCategoryResponse> getAllAmountSumsByCategoryByCategoryName(@Param("name") String name);

}
