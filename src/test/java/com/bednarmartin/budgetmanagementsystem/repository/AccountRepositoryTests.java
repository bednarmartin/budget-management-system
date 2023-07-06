package com.bednarmartin.budgetmanagementsystem.repository;

import com.bednarmartin.budgetmanagementsystem.db.model.Account;
import com.bednarmartin.budgetmanagementsystem.db.model.AccountType;
import com.bednarmartin.budgetmanagementsystem.db.repository.AccountRepository;
import com.bednarmartin.budgetmanagementsystem.db.repository.AccountTypeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AccountRepositoryTests {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountTypeRepository accountTypeRepository;

    @Test
    @Transactional
    public void testUpdateAccountById() {
        AccountType accountType = AccountType.builder()
                .name("Cash")
                .build();

        Account account = Account.builder()
                .name("Cash")
                .balance(BigDecimal.ZERO)
                .accountType(accountType)
                .build();
        accountTypeRepository.save(accountType);
        accountRepository.save(account);

        String newName = "Bank Account";
        accountRepository.updateAccountById(accountType.getId(), newName, accountType);

        Optional<Account> updatedAccountOptional = accountRepository.findById(account.getId());
        Assertions.assertTrue(updatedAccountOptional.isPresent());
        Account updatedAccount = updatedAccountOptional.get();

        Assertions.assertEquals(accountType.getId(), updatedAccount.getId());
        Assertions.assertEquals(newName, updatedAccount.getName());
    }
}
