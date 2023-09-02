package com.bednarmartin.budgetmanagementsystem.controller.graphql;

import com.bednarmartin.budgetmanagementsystem.service.api.response.TransactionResponse;
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

import static com.bednarmartin.budgetmanagementsystem.db.model.enums.TransactionType.EXPENSE;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;

@SpringBootTest(webEnvironment = MOCK)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureGraphQlTester
public class TransactionGraphqlControllerTests {

    private final String accountTypeName = "Cash";
    private final String accountName = "Wallet";
    private final BigDecimal initialBalance = BigDecimal.valueOf(12.56);
    String categoryName = "Groceries";
    @Autowired
    private GraphQlTester tester;

    @BeforeEach
    public void init() {
        String createCategoryDocument = """
                mutation createCategory($name: String, $transactionType: TransactionType) {
                     createCategory(request: {name: $name, transactionType: $transactionType}) {
                       id
                       name
                       transactionType
                     }
                   }
                """;

        tester.document(createCategoryDocument)
                .variable("name", categoryName)
                .variable("transactionType", EXPENSE)
                .execute();


        String createAccountTypeDocument = """
                mutation createAccountType($name: String) {
                    createAccountType(request: {name: $name}) {
                      id
                      name
                    }
                  }
                """;
        tester.document(createAccountTypeDocument)
                .variable("name", accountTypeName)
                .execute();


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
                .execute();
    }

    @Test
    void shouldCreateNewTransaction() {
        BigDecimal amount = BigDecimal.valueOf(10.23);
        String description = "Food";

        String document = """
                mutation createTransaction($amount: Float, $description: String, $categoryName: String, $transactionType: TransactionType, $accountName: String) {
                       createTransaction(
                         request: {amount: $amount, description: $description, categoryName: $categoryName, type: $transactionType, accountName: $accountName}
                       ) {
                         id
                         amount
                         description
                         type
                         account {
                           id
                           name
                           balance
                           accountType {
                             id
                             name
                           }
                         }
                         category {
                           id
                           name
                           transactionType
                         }
                       }
                     }
                           """;
        tester.document(document)
                .variable("amount", amount)
                .variable("description", description)
                .variable("categoryName", categoryName)
                .variable("transactionType", EXPENSE)
                .variable("accountName", accountName)
                .execute()
                .path("createTransaction")
                .entity(TransactionResponse.class)
                .satisfies(transactionResponse -> {
                    Assertions.assertEquals(1L, transactionResponse.getId());
                    Assertions.assertEquals(accountName, transactionResponse.getAccount().getName());
                    Assertions.assertEquals(description, transactionResponse.getDescription());
                    Assertions.assertEquals(amount, transactionResponse.getAmount());
                    Assertions.assertEquals(accountTypeName, transactionResponse.getAccount().getAccountType().getName());
                    Assertions.assertEquals(EXPENSE, transactionResponse.getType());
                    Assertions.assertEquals(initialBalance.subtract(amount), transactionResponse.getAccount().getBalance());
                });
    }

    @Test
    void shouldUpdateTransaction() {
        BigDecimal amount = BigDecimal.valueOf(10.23);
        String description = "Food";

        String document =
                """
                        mutation createTransaction($amount: Float, $description: String, $categoryName: String, $transactionType: TransactionType, $accountName: String) {
                               createTransaction(
                                 request: {amount: $amount, description: $description, categoryName: $categoryName, type: $transactionType, accountName: $accountName}
                               ) {
                                 id
                                 amount
                                 description
                                 type
                                 account {
                                   id
                                   name
                                   balance
                                   accountType {
                                     id
                                     name
                                   }
                                 }
                                 category {
                                   id
                                   name
                                   transactionType
                                 }
                               }
                             }
                                   """;
        tester.document(document)
                .variable("amount", amount)
                .variable("description", description)
                .variable("categoryName", categoryName)
                .variable("transactionType", EXPENSE)
                .variable("accountName", accountName)
                .execute();

        BigDecimal newAmount = BigDecimal.valueOf(20.23);
        String newDescription = "Food Delivery";

        String updateDocument =
                """
                        mutation updateTransaction($id: ID!, $amount: Float, $description: String, $categoryName: String, $transactionType: TransactionType, $accountName: String) {
                           updateTransaction(
                             id: $id
                             request: {amount: $amount, description: $description, categoryName: $categoryName, type: $transactionType, accountName: $accountName}
                           ) {
                             id
                             amount
                             description
                             type
                             account {
                               id
                               name
                               balance
                               accountType {
                                 id
                                 name
                               }
                             }
                             category {
                               id
                               name
                               transactionType
                             }
                           }
                         }
                        """;

        tester.document(updateDocument)
                .variable("id", 1L)
                .variable("amount", newAmount)
                .variable("description", newDescription)
                .variable("categoryName", categoryName)
                .variable("transactionType", EXPENSE)
                .variable("accountName", accountName)
                .execute()
                .path("updateTransaction")
                .entity(TransactionResponse.class)
                .satisfies(transactionResponse -> {
                    Assertions.assertEquals(1L, transactionResponse.getId());
                    Assertions.assertEquals(accountName, transactionResponse.getAccount().getName());
                    Assertions.assertEquals(newDescription, transactionResponse.getDescription());
                    Assertions.assertEquals(newAmount, transactionResponse.getAmount());
                    Assertions.assertEquals(accountTypeName, transactionResponse.getAccount().getAccountType().getName());
                    Assertions.assertEquals(EXPENSE, transactionResponse.getType());
                    Assertions.assertEquals(initialBalance.subtract(newAmount), transactionResponse.getAccount().getBalance());

                });

    }

    @Test
    void shouldDeleteTransaction() {
        Long id = 1L;
        BigDecimal amount = BigDecimal.valueOf(10.23);
        String description = "Food";

        String document =
                """
                        mutation createTransaction($amount: Float, $description: String, $categoryName: String, $transactionType: TransactionType, $accountName: String) {
                               createTransaction(
                                 request: {amount: $amount, description: $description, categoryName: $categoryName, type: $transactionType, accountName: $accountName}
                               ) {
                                 id
                                 amount
                                 description
                                 type
                                 account {
                                   id
                                   name
                                   balance
                                   accountType {
                                     id
                                     name
                                   }
                                 }
                                 category {
                                   id
                                   name
                                   transactionType
                                 }
                               }
                             }
                                   """;
        tester.document(document)
                .variable("amount", amount)
                .variable("description", description)
                .variable("categoryName", categoryName)
                .variable("transactionType", EXPENSE)
                .variable("accountName", accountName)
                .execute();

        String deleteDocument = """
                mutation deleteTransaction($id: ID!) {
                   deleteTransaction(id: $id)
                 }
                """;

        tester.document(deleteDocument)
                .variable("id", id)
                .execute()
                .path("deleteTransaction")
                .entity(String.class)
                .satisfies(string -> Assertions.assertEquals(string, "Transaction with id: " + id + " deleted"));

    }

    @Test
    void shouldGetTransactionById() {
        Long id = 1L;
        BigDecimal amount = BigDecimal.valueOf(10.23);
        String description = "Food";

        String document =
                """
                        mutation createTransaction($amount: Float, $description: String, $categoryName: String, $transactionType: TransactionType, $accountName: String) {
                               createTransaction(
                                 request: {amount: $amount, description: $description, categoryName: $categoryName, type: $transactionType, accountName: $accountName}
                               ) {
                                 id
                                 amount
                                 description
                                 type
                                 account {
                                   id
                                   name
                                   balance
                                   accountType {
                                     id
                                     name
                                   }
                                 }
                                 category {
                                   id
                                   name
                                   transactionType
                                 }
                               }
                             }
                                   """;
        tester.document(document)
                .variable("amount", amount)
                .variable("description", description)
                .variable("categoryName", categoryName)
                .variable("transactionType", EXPENSE)
                .variable("accountName", accountName)
                .execute();


        String queryDocument = """
                query getTransactionById($id: ID!){
                       getTransactionById(id: $id){
                         id
                         amount
                         description
                         type
                         account{
                           id
                           name
                           balance
                           accountType{
                             id
                             name
                           }
                         }
                         category{
                           id
                           name
                           transactionType
                         }
                       }
                     }
                """;

        tester.document(queryDocument)
                .variable("id", id)
                .execute()
                .path("getTransactionById")
                .entity(TransactionResponse.class)
                .satisfies(transactionResponse -> {
                    Assertions.assertEquals(1L, transactionResponse.getId());
                    Assertions.assertEquals(accountName, transactionResponse.getAccount().getName());
                    Assertions.assertEquals(description, transactionResponse.getDescription());
                    Assertions.assertEquals(amount, transactionResponse.getAmount());
                    Assertions.assertEquals(accountTypeName, transactionResponse.getAccount().getAccountType().getName());
                    Assertions.assertEquals(EXPENSE, transactionResponse.getType());
                    Assertions.assertEquals(initialBalance.subtract(amount), transactionResponse.getAccount().getBalance());


                });

    }

    @Test
    void shouldGetAllAccounts() {
        BigDecimal[] amounts = {BigDecimal.valueOf(10.23), BigDecimal.valueOf(20.23), BigDecimal.valueOf(30.33)};
        String[] descriptions = {"Food", "Food delivery", "Alcohol"};

        for (int i = 0; i < amounts.length; i++) {
            String createDocument = """
                    mutation createTransaction($amount: Float, $description: String, $categoryName: String, $transactionType: TransactionType, $accountName: String) {
                           createTransaction(
                             request: {amount: $amount, description: $description, categoryName: $categoryName, type: $transactionType, accountName: $accountName}
                           ) {
                             id
                             amount
                             description
                             type
                             account {
                               id
                               name
                               balance
                               accountType {
                                 id
                                 name
                               }
                             }
                             category {
                               id
                               name
                               transactionType
                             }
                           }
                         }
                               """;
            tester.document(createDocument)
                    .variable("amount", amounts[i])
                    .variable("description", descriptions[i])
                    .variable("categoryName", categoryName)
                    .variable("transactionType", EXPENSE)
                    .variable("accountName", accountName)
                    .execute();

        }


        String queryDocument = """
                query getAllTransactions{
                     getAllTransactions{
                       id
                       amount
                       description
                       type
                       account{
                         id
                         name
                         balance
                         accountType{
                           id
                           name
                         }
                       }
                       category{
                         id
                         name
                         transactionType
                       }
                     }
                   }
                """;

        List<TransactionResponse> responseList = tester.document(queryDocument)
                .execute()
                .path("getAllTransactions")
                .entityList(TransactionResponse.class)
                .get();

        Assertions.assertEquals(amounts.length, responseList.size());
        BigDecimal balance = initialBalance;
        for (int i = 0; i < responseList.size(); i++) {
            balance = balance.subtract(amounts[i]);
        }
        for (int i = 0; i < responseList.size(); i++) {
            Assertions.assertEquals(i + 1, responseList.get(i).getId());
            Assertions.assertEquals(accountName, responseList.get(i).getAccount().getName());
            Assertions.assertEquals(descriptions[i], responseList.get(i).getDescription());
            Assertions.assertEquals(amounts[i], responseList.get(i).getAmount());
            Assertions.assertEquals(accountTypeName, responseList.get(i).getAccount().getAccountType().getName());
            Assertions.assertEquals(EXPENSE, responseList.get(i).getType());
            Assertions.assertEquals(balance, responseList.get(i).getAccount().getBalance());
        }
    }
}
