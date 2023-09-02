package com.bednarmartin.budgetmanagementsystem.service;

import com.bednarmartin.budgetmanagementsystem.service.api.AccountService;
import com.bednarmartin.budgetmanagementsystem.service.api.AccountTypeService;
import com.bednarmartin.budgetmanagementsystem.service.api.request.AccountTypeRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.request.CreateAccountRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.request.UpdateAccountRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.response.AccountResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AccountServiceTests {

    @Autowired
    private AccountTypeService accountTypeService;

    @Autowired
    private AccountService accountService;

    @BeforeEach
    public void init() {
        AccountTypeRequest request = AccountTypeRequest.builder()
                .name("Cash")
                .build();
        accountTypeService.addAccountType(request);
    }

    @Test
    public void testAddOneAccount() {
        CreateAccountRequest request = CreateAccountRequest.builder()
                .name("Cash")
                .accountTypeName("Cash")
                .initialBalance(BigDecimal.valueOf(26.59))
                .build();

        accountService.addAccount(request);

        List<AccountResponse> responses = accountService.getAllAccounts();

        Assertions.assertEquals(1, responses.size());
        Assertions.assertEquals(1L, responses.get(0).getId());
        Assertions.assertEquals("Cash", responses.get(0).getName());
        Assertions.assertEquals("Cash", responses.get(0).getAccountType().getName());
        Assertions.assertEquals(BigDecimal.valueOf(26.59), responses.get(0).getBalance());

    }

    @Test
    public void testAddMoreAccountTypes() {
        String[] names = {"Cash", "Bank Account", "Investments"};
        BigDecimal[] balances = {BigDecimal.valueOf(13.69), BigDecimal.valueOf(1559.99), BigDecimal.valueOf(89963.59)};
        for (int i = 0; i < names.length; i++) {
            CreateAccountRequest request = CreateAccountRequest.builder()
                    .name(names[i])
                    .accountTypeName("Cash")
                    .initialBalance(balances[i])
                    .build();

            accountService.addAccount(request);
        }

        List<AccountResponse> responses = accountService.getAllAccounts();
        Assertions.assertEquals(3, responses.size());
    }

    @Test
    public void testUpdateAccountType() {
        CreateAccountRequest request = CreateAccountRequest.builder()
                .name("Cash")
                .accountTypeName("Cash")
                .initialBalance(BigDecimal.valueOf(58.54))
                .build();

        accountService.addAccount(request);


        AccountResponse response = accountService.getAllAccounts().get(0);
        UpdateAccountRequest updateRequest = UpdateAccountRequest.builder()
                .name("Bank Account")
                .accountTypeName("Cash")
                .build();
        accountService.updateAccount(response.getId(), updateRequest);

        AccountResponse updatedResponse = accountService.getAccountById(response.getId());
        Assertions.assertEquals("Bank Account", updatedResponse.getName());
        Assertions.assertEquals("Cash", updatedResponse.getAccountType().getName());
        Assertions.assertEquals(BigDecimal.valueOf(58.54), updatedResponse.getBalance());
    }

    @Test
    public void testDeleteAccountTypeById() {
        CreateAccountRequest request = CreateAccountRequest.builder()
                .name("Cash")
                .accountTypeName("Cash")
                .initialBalance(BigDecimal.ZERO)
                .build();

        accountService.addAccount(request);

        AccountResponse response = accountService.getAllAccounts().get(0);

        accountService.deleteAccountById(response.getId());
        List<AccountResponse> responses = accountService.getAllAccounts();

        Assertions.assertTrue(responses.isEmpty());

    }


}
