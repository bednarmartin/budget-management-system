package com.bednarmartin.budgetmanagementsystem.controller.graphql;

import com.bednarmartin.budgetmanagementsystem.service.api.response.AccountTypeResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;

@SpringBootTest(webEnvironment = MOCK)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureGraphQlTester
public class AccountTypeGraphqlControllerTests {

    @Autowired
    private GraphQlTester tester;

    @Test
    void shouldCreateNewAccountType() {
        String accountTypeName = "Cash";
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
                .execute()
                .path("createAccountType")
                .entity(AccountTypeResponse.class)
                .satisfies(accountTypeResponse -> {
                    Assertions.assertEquals(accountTypeName, accountTypeResponse.getName());
                    Assertions.assertEquals(1L, accountTypeResponse.getId());
                });
    }

    @Test
    void shouldUpdateAccountType() {
        String accountTypeName = "Cash";
        String createDocument = """
                mutation createAccountType($name: String) {
                    createAccountType(request: {name: $name}) {
                      id
                      name
                    }
                  }
                """;
        tester.document(createDocument)
                .variable("name", accountTypeName)
                .execute();

        String newAccountTypeName = "Bank Account";
        String updateDocument = """
                mutation updateAccountType($id: ID!, $name: String) {
                  updateAccountType(id: $id, request: {name: $name}) {
                    id
                    name
                  }
                }
                """;

        tester.document(updateDocument)
                .variable("name", newAccountTypeName)
                .variable("id", 1L)
                .execute()
                .path("updateAccountType")
                .entity(AccountTypeResponse.class)
                .satisfies(accountTypeResponse -> {
                    Assertions.assertEquals(1L, accountTypeResponse.getId());
                    Assertions.assertEquals(newAccountTypeName, accountTypeResponse.getName());
                });

    }

    @Test
    void shouldDeleteAccountType() {
        String accountTypeName = "Cash";
        Long id = 1L;
        String createDocument = """
                mutation createAccountType($name: String) {
                    createAccountType(request: {name: $name}) {
                      id
                      name
                    }
                  }
                """;
        tester.document(createDocument)
                .variable("name", accountTypeName)
                .execute();

        String deleteDocument = """
                mutation deleteAccountType($id: ID!) {
                   deleteAccountType(id: $id)
                 }
                """;

        tester.document(deleteDocument)
                .variable("id", id)
                .execute()
                .path("deleteAccountType")
                .entity(String.class)
                .satisfies(string -> Assertions.assertEquals(string, "Account Type with id: " + id + " deleted"));

    }

    @Test
    void shouldGetAccountTypeById() {
        String accountTypeName = "Cash";
        Long id = 1L;
        String createDocument = """
                mutation createAccountType($name: String) {
                    createAccountType(request: {name: $name}) {
                      id
                      name
                    }
                  }
                """;
        tester.document(createDocument)
                .variable("name", accountTypeName)
                .execute();

        String queryDocument = """
                query getAccountTypeById($id: ID!){
                  getAccountTypeById(id: $id){
                    id
                    name
                  }
                }
                """;

        tester.document(queryDocument)
                .variable("id", id)
                .execute()
                .path("getAccountTypeById")
                .entity(AccountTypeResponse.class)
                .satisfies(accountTypeResponse -> {
                    Assertions.assertEquals(id, accountTypeResponse.getId());
                    Assertions.assertEquals(accountTypeName, accountTypeResponse.getName());
                });

    }

    @Test
    void shouldGetAllAccountTypes() {
        String[] accountTypeNames = {"Cash", "Bank Account", "Investments"};
        for (String accountTypeName : accountTypeNames) {
            String createDocument = """
                    mutation createAccountType($name: String) {
                        createAccountType(request: {name: $name}) {
                          id
                          name
                        }
                      }
                    """;
            tester.document(createDocument)
                    .variable("name", accountTypeName)
                    .execute();
        }


        String queryDocument = """
                query getAllAccountTypes {
                    getAllAccountTypes {
                      id
                      name
                    }
                  }
                """;

        List<AccountTypeResponse> responseList = tester.document(queryDocument)
                .execute()
                .path("getAllAccountTypes")
                .entityList(AccountTypeResponse.class)
                .get();

        Assertions.assertEquals(accountTypeNames.length, responseList.size());

        for (int i = 0; i < responseList.size(); i++) {
            Assertions.assertEquals((i + 1), responseList.get(i).getId());
            Assertions.assertEquals(accountTypeNames[i], responseList.get(i).getName());
        }

    }
}
