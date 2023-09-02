package com.bednarmartin.budgetmanagementsystem.controller.graphql;

import com.bednarmartin.budgetmanagementsystem.service.api.response.AccountResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;

@SpringBootTest(webEnvironment = MOCK)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureGraphQlTester
public class AccountGraphqlControllerTests {

    private final String accountTypeName = "Cash";
    @Autowired
    private GraphQlTester tester;

    @BeforeEach
    public void init() {
        String document = """
                mutation createAccountType($name: String) {
                    createAccountType(request: {name: $name}) {
                      id
                      name
                    }
                  }
                """;
        tester.document(document)
                .variable("name", accountTypeName)
                .execute();
    }

    @Test
    void shouldCreateNewAccount() {
        String accountName = "Wallet";
        BigDecimal initialBalance = BigDecimal.valueOf(12.56);

        String document = """
                mutation createAccount($name: String, $initialBalance: Float, $accountTypeName: String) {
                     createAccount(request:{
                       name: $name
                       initialBalance: $initialBalance
                       accountTypeName: $accountTypeName
                     }){
                       id
                       name
                       balance
                       accountType{
                         id
                         name
                       }
                     }
                   }
                           """;
        tester.document(document)
                .variable("name", accountName)
                .variable("initialBalance", initialBalance)
                .variable("accountTypeName", accountTypeName)
                .execute()
                .path("createAccount")
                .entity(AccountResponse.class)
                .satisfies(accountResponse -> {
                    Assertions.assertEquals(1L, accountResponse.getId());
                    Assertions.assertEquals(accountName, accountResponse.getName());
                    Assertions.assertEquals(initialBalance, accountResponse.getBalance());
                    Assertions.assertEquals(accountTypeName, accountResponse.getAccountType().getName());

                });
    }

    @Test
    void shouldUpdateAccount() {
        String accountName = "Wallet";
        BigDecimal initialBalance = BigDecimal.valueOf(12.56);

        String createDocument = """
                mutation createAccount($name: String, $initialBalance: Float, $accountTypeName: String) {
                     createAccount(request:{
                       name: $name
                       initialBalance: $initialBalance
                       accountTypeName: $accountTypeName
                     }){
                       id
                       name
                       balance
                       accountType{
                         id
                         name
                       }
                     }
                   }
                           """;
        tester.document(createDocument)
                .variable("name", accountName)
                .variable("initialBalance", initialBalance)
                .variable("accountTypeName", accountTypeName)
                .execute();

        String newAccountName = "Pocket";

        String updateDocument = """
                mutation updateAccount($id: ID!, $name: String, $accountTypeName: String) {
                  updateAccount(id: $id,\s
                    request: {name: $name, accountTypeName: $accountTypeName}
                  ) {
                    id
                    name
                    balance
                    accountType {
                      id
                      name
                    }
                  }
                }
                """;

        tester.document(updateDocument)
                .variable("id", 1L)
                .variable("name", newAccountName)
                .variable("accountTypeName", accountTypeName)
                .execute()
                .path("updateAccount")
                .entity(AccountResponse.class)
                .satisfies(accountResponse -> {
                    Assertions.assertEquals(1L, accountResponse.getId());
                    Assertions.assertEquals(newAccountName, accountResponse.getName());
                    Assertions.assertEquals(initialBalance, accountResponse.getBalance());
                    Assertions.assertEquals(accountTypeName, accountResponse.getAccountType().getName());

                });

    }

    @Test
    void shouldDeleteAccount() {
        String accountName = "Wallet";
        BigDecimal initialBalance = BigDecimal.valueOf(12.56);
        Long id = 1L;

        String createDocument = """
                mutation createAccount($name: String, $initialBalance: Float, $accountTypeName: String) {
                     createAccount(request:{
                       name: $name
                       initialBalance: $initialBalance
                       accountTypeName: $accountTypeName
                     }){
                       id
                       name
                       balance
                       accountType{
                         id
                         name
                       }
                     }
                   }
                           """;
        tester.document(createDocument)
                .variable("name", accountName)
                .variable("initialBalance", initialBalance)
                .variable("accountTypeName", accountTypeName)
                .execute();

        String deleteDocument = """
                mutation deleteAccount($id: ID!) {
                   deleteAccount(id: $id)
                 }
                """;

        tester.document(deleteDocument)
                .variable("id", id)
                .execute()
                .path("deleteAccount")
                .entity(String.class)
                .satisfies(string -> Assertions.assertEquals(string, "Account with id: " + id + " deleted"));

    }

    @Test
    void shouldGetAccountById() {
        String accountName = "Wallet";
        BigDecimal initialBalance = BigDecimal.valueOf(12.56);
        Long id = 1L;

        String createDocument = """
                mutation createAccount($name: String, $initialBalance: Float, $accountTypeName: String) {
                  createAccount(
                    request: {name: $name, initialBalance: $initialBalance, accountTypeName: $accountTypeName}
                  ) {
                    id
                    name
                    balance
                    accountType {
                      id
                      name
                    }
                  }
                }
                                           """;
        tester.document(createDocument)
                .variable("name", accountName)
                .variable("initialBalance", initialBalance)
                .variable("accountTypeName", accountTypeName)
                .execute();

        String queryDocument = """
                query getAccountById($id: ID!) {
                     getAccountById(id: $id) {
                       id
                       name
                       balance
                       accountType {
                         id
                         name
                       }
                     }
                   }
                """;

        tester.document(queryDocument)
                .variable("id", id)
                .execute()
                .path("getAccountById")
                .entity(AccountResponse.class)
                .satisfies(accountResponse -> {
                    Assertions.assertEquals(1L, accountResponse.getId());
                    Assertions.assertEquals(accountName, accountResponse.getName());
                    Assertions.assertEquals(initialBalance, accountResponse.getBalance());
                    Assertions.assertEquals(accountTypeName, accountResponse.getAccountType().getName());

                });

    }

    @Test
    void shouldGetAllAccounts() {
        String[] accountNames = {"Wallet", "Bank Account", "Investments"};
        BigDecimal[] accountInitialBalances = {BigDecimal.valueOf(12.56), BigDecimal.valueOf(-58.96), BigDecimal.valueOf(112.69)};
        for (int i = 0; i < accountNames.length; i++) {
            String createDocument = """
                    mutation createAccount($name: String, $initialBalance: Float, $accountTypeName: String) {
                      createAccount(
                        request: {name: $name, initialBalance: $initialBalance, accountTypeName: $accountTypeName}
                      ) {
                        id
                      }
                    }
                    """;
            tester.document(createDocument)
                    .variable("name", accountNames[i])
                    .variable("initialBalance", accountInitialBalances[i])
                    .variable("accountTypeName", accountTypeName)
                    .execute();
        }


        String queryDocument = """
                query getAllAccounts {
                    getAllAccounts {
                       id
                       name
                       balance
                       accountType{
                         id
                         name
                       }
                     }
                  }
                """;

        List<AccountResponse> responseList = tester.document(queryDocument)
                .execute()
                .path("getAllAccounts")
                .entityList(AccountResponse.class)
                .get();

        Assertions.assertEquals(accountNames.length, responseList.size());

        for (int i = 0; i < responseList.size(); i++) {
            Assertions.assertEquals((i + 1), responseList.get(i).getId());
            Assertions.assertEquals(accountNames[i], responseList.get(i).getName());
            Assertions.assertEquals(accountTypeName, responseList.get(i).getAccountType().getName());
            Assertions.assertEquals(accountInitialBalances[i], responseList.get(i).getBalance());
        }

    }
}
