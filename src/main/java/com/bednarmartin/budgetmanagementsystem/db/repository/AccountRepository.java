package com.bednarmartin.budgetmanagementsystem.db.repository;

import com.bednarmartin.budgetmanagementsystem.db.model.Account;
import com.bednarmartin.budgetmanagementsystem.db.model.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface AccountRepository extends JpaRepository<Account, Long> {

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Account SET name = :name, accountType = :account_type WHERE id = :id")
    void updateAccountById(@Param("id") long id,
                           @Param("name") String name,
                           @Param("account_type") AccountType accountType);
}
