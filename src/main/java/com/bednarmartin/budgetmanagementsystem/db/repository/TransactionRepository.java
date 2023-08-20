package com.bednarmartin.budgetmanagementsystem.db.repository;

import com.bednarmartin.budgetmanagementsystem.annotations.LogMethod;
import com.bednarmartin.budgetmanagementsystem.db.model.Account;
import com.bednarmartin.budgetmanagementsystem.db.model.Category;
import com.bednarmartin.budgetmanagementsystem.db.model.Transaction;
import com.bednarmartin.budgetmanagementsystem.db.model.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @LogMethod
    @Modifying(clearAutomatically = true)
    @Query("""
            UPDATE Transaction SET amount = :amount, description = :description, category = :category, type = :type,
            dateUpdated = :datetime, account = :account WHERE id = :id
            """)
    void updateTransactionById(@Param("id") long id,
                               @Param("amount") BigDecimal amount,
                               @Param("description") String description,
                               @Param("category") Category category,
                               @Param("type") TransactionType type,
                               @Param("account") Account account,
                               @Param("datetime") LocalDateTime dateTime);

}

