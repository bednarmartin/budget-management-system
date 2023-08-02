package com.bednarmartin.budgetmanagementsystem.db.repository;

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
    Optional<Category> findByName(String name);

    void deleteByName(String name);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Category SET name = :name, dateUpdated = :datetime, transactionType = :transaction_type  WHERE id = :id")
    void updateCategoryById(@Param("id") long id,
                            @Param("name") String name,
                            @Param("transaction_type") TransactionType type,
                            @Param("datetime") LocalDateTime dateTime);

    @Query("""
            SELECT
            new com.bednarmartin.budgetmanagementsystem.service.api.response.AmountSumByCategoryResponse(c, SUM(t.amount))
            FROM Category c
            LEFT OUTER JOIN Transaction t ON c.id = t.category.id
            GROUP BY c.name
            """)
    List<AmountSumByCategoryResponse> getAllAmountSumsByCategory();

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
