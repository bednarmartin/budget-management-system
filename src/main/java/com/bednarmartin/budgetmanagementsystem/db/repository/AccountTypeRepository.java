package com.bednarmartin.budgetmanagementsystem.db.repository;

import com.bednarmartin.budgetmanagementsystem.annotations.LogMethod;
import com.bednarmartin.budgetmanagementsystem.db.model.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface AccountTypeRepository extends JpaRepository<AccountType, Long> {

    Optional<AccountType> findByName(String name);

    void deleteByName(String name);
    @LogMethod
    @Modifying(clearAutomatically = true)
    @Query("UPDATE AccountType SET name = :name WHERE id = :id")
    void updateAccountTypeById(@Param("id") long id,
                               @Param("name") String name);
}
