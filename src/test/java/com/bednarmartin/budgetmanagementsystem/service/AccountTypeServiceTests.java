package com.bednarmartin.budgetmanagementsystem.service;

import com.bednarmartin.budgetmanagementsystem.db.model.AccountType;
import com.bednarmartin.budgetmanagementsystem.service.api.AccountTypeService;
import com.bednarmartin.budgetmanagementsystem.service.api.request.AccountTypeRequest;
import com.bednarmartin.budgetmanagementsystem.service.api.response.AccountTypeResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AccountTypeServiceTests {

    @Autowired
    private AccountTypeService service;

    @Test
    public void testAddOneAccountType() {
        AccountTypeRequest request = AccountTypeRequest.builder()
                .name("Cash")
                .build();

        service.addAccountType(request);

        List<AccountTypeResponse> responses = service.getAllAccountTypes();

        Assertions.assertEquals(1, responses.size());
        Assertions.assertEquals(1L, responses.get(0).getId());
        Assertions.assertEquals("Cash", responses.get(0).getName());

    }

    @Test
    public void testAddMoreAccountTypes() {
        String[] names = {"Cash", "Bank Account", "Investments"};

        for (String name : names) {
            AccountTypeRequest request = AccountTypeRequest.builder()
                    .name(name)
                    .build();
            service.addAccountType(request);
        }

        List<AccountTypeResponse> responses = service.getAllAccountTypes();
        Assertions.assertEquals(3, responses.size());
    }

    @Test
    public void testUpdateAccountType() {
        AccountTypeRequest request = AccountTypeRequest.builder()
                .name("Cash")
                .build();

        service.addAccountType(request);

        AccountTypeResponse response = service.getAllAccountTypes().get(0);
        AccountTypeRequest updateRequest = AccountTypeRequest.builder()
                .name("Bank Account")
                .build();
        service.updateAccountType(response.getId(), updateRequest);

        AccountTypeResponse updatedResponse = service.getAccountTypeById(response.getId());
        Assertions.assertEquals("Bank Account", updatedResponse.getName());
    }

    @Test
    public void testDeleteAccountTypeById() {
        AccountTypeRequest request = AccountTypeRequest.builder()
                .name("Cash")
                .build();

        service.addAccountType(request);

        AccountTypeResponse response = service.getAllAccountTypes().get(0);

        service.deleteAccountTypeById(response.getId());
        List<AccountTypeResponse> responses = service.getAllAccountTypes();

        Assertions.assertTrue(responses.isEmpty());

    }

    @Test
    public void testDeleteAccountTypeByName() {
        String name = "Cash";
        AccountTypeRequest request = AccountTypeRequest.builder()
                .name(name)
                .build();

        service.addAccountType(request);

        service.deleteAccountTypeByName(name);
        List<AccountTypeResponse> responses = service.getAllAccountTypes();

        Assertions.assertTrue(responses.isEmpty());
    }

    @Test
    public void testGetAccountTypeByName() {
        String name = "Cash";
        AccountTypeRequest request = AccountTypeRequest.builder()
                .name(name)
                .build();

        service.addAccountType(request);
        AccountType accountType = service.getAccountTypeByName(name);

        Assertions.assertNotNull(accountType);
        Assertions.assertEquals(name, accountType.getName());

    }
}
