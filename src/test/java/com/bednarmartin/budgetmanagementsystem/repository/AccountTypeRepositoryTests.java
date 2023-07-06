package com.bednarmartin.budgetmanagementsystem.repository;

import com.bednarmartin.budgetmanagementsystem.db.model.AccountType;
import com.bednarmartin.budgetmanagementsystem.db.repository.AccountTypeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AccountTypeRepositoryTests {

    @Autowired
    private AccountTypeRepository repository;

    @Test
    @Transactional
    public void testUpdateAccountTypeById() {
        AccountType accountType = AccountType.builder()
                .name("Cash")
                .build();
        repository.save(accountType);

        String newName = "Bank Account";
        repository.updateAccountTypeById(accountType.getId(), newName);

        Optional<AccountType> updatedAccountTypeOptional = repository.findById(accountType.getId());
        Assertions.assertTrue(updatedAccountTypeOptional.isPresent());
        AccountType updatedAccountType = updatedAccountTypeOptional.get();

        Assertions.assertEquals(accountType.getId(), updatedAccountType.getId());
        Assertions.assertEquals(newName, updatedAccountType.getName());
    }
}
