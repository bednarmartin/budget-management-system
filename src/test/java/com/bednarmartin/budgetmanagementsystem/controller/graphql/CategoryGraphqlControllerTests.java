package com.bednarmartin.budgetmanagementsystem.controller.graphql;

import com.bednarmartin.budgetmanagementsystem.db.model.enums.TransactionType;
import com.bednarmartin.budgetmanagementsystem.service.api.response.CategoryResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.bednarmartin.budgetmanagementsystem.db.model.enums.TransactionType.EXPENSE;
import static com.bednarmartin.budgetmanagementsystem.db.model.enums.TransactionType.INCOME;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;

@SpringBootTest(webEnvironment = MOCK)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureGraphQlTester
public class CategoryGraphqlControllerTests {

    @Autowired
    private GraphQlTester tester;

    @Test
    void shouldCreateNewCategory() {
        String categoryName = "Groceries";
        TransactionType transactionType = EXPENSE;

        String document = """
                mutation createCategory($name: String, $transactionType: TransactionType) {
                     createCategory(request: {name: $name, transactionType: $transactionType}) {
                       id
                       name
                       transactionType
                     }
                   }
                """;
        tester.document(document)
                .variable("name", categoryName)
                .variable("transactionType", transactionType)
                .execute()
                .path("createCategory")
                .entity(CategoryResponse.class)
                .satisfies(categoryResponse -> {
                    Assertions.assertEquals(categoryName, categoryResponse.getName());
                    Assertions.assertEquals(transactionType, categoryResponse.getTransactionType());
                });
    }

    @Test
    void shouldUpdateCategory() {
        String categoryName = "Groceries";

        String document = """
                mutation createCategory($name: String, $transactionType: TransactionType) {
                     createCategory(request: {name: $name, transactionType: $transactionType}) {
                       id
                       name
                       transactionType
                     }
                   }
                """;
        tester.document(document)
                .variable("name", categoryName)
                .variable("transactionType", EXPENSE)
                .execute();

        String newCategoryName = "Utilities";
        TransactionType newTransactionType = EXPENSE;
        String updateDocument = """
                mutation updateCategory($id: ID!, $name: String, $transactionType: TransactionType) {
                   updateCategory(id: $id, request: {name: $name, transactionType: $transactionType}) {
                     id
                     name
                     transactionType
                   }
                 }
                """;

        tester.document(updateDocument)
                .variable("name", newCategoryName)
                .variable("transactionType", newTransactionType)
                .variable("id", 1L)
                .execute()
                .path("updateCategory")
                .entity(CategoryResponse.class)
                .satisfies(categoryResponse -> {
                    Assertions.assertEquals(1L, categoryResponse.getId());
                    Assertions.assertEquals(newCategoryName, categoryResponse.getName());
                    Assertions.assertEquals(newTransactionType, categoryResponse.getTransactionType());
                });

    }

    @Test
    void shouldDeleteCategory() {
        Long id = 1L;
        String categoryName = "Groceries";

        String document = """
                mutation createCategory($name: String, $transactionType: TransactionType) {
                     createCategory(request: {name: $name, transactionType: $transactionType}) {
                       id
                       name
                       transactionType
                     }
                   }
                """;
        tester.document(document)
                .variable("name", categoryName)
                .variable("transactionType", EXPENSE)
                .execute();

        String deleteDocument = """
                mutation deleteCategory($id: ID!) {
                   deleteCategory(id: $id)
                 }
                """;

        tester.document(deleteDocument)
                .variable("id", id)
                .execute()
                .path("deleteCategory")
                .entity(String.class)
                .satisfies(string -> Assertions.assertEquals(string, "Category with id: " + id + " deleted"));

    }

    @Test
    void shouldGetCategoryById() {
        Long id = 1L;
        String categoryName = "Groceries";
        TransactionType transactionType = EXPENSE;

        String document = """
                mutation createCategory($name: String, $transactionType: TransactionType) {
                     createCategory(request: {name: $name, transactionType: $transactionType}) {
                       id
                       name
                       transactionType
                     }
                   }
                """;
        tester.document(document)
                .variable("name", categoryName)
                .variable("transactionType", transactionType)
                .execute();

        String queryDocument = """
                query getCategoryById($id: ID!){
                  getCategoryById(id: $id){
                     id
                     name
                     transactionType
                  }
                }
                """;

        tester.document(queryDocument)
                .variable("id", id)
                .execute()
                .path("getCategoryById")
                .entity(CategoryResponse.class)
                .satisfies(categoryResponse -> {
                    Assertions.assertEquals(1L, categoryResponse.getId());
                    Assertions.assertEquals(categoryName, categoryResponse.getName());
                    Assertions.assertEquals(transactionType, categoryResponse.getTransactionType());
                });

    }

    @Test
    void shouldGetAllCategory() {
        String[] categoryNames = {"Groceries", "Utilities", "Health"};
        TransactionType[] transactionTypes = {EXPENSE, EXPENSE, INCOME};
        for (int i = 0; i < categoryNames.length; i++) {
            String createDocument = """
                     mutation createCategory($name: String, $transactionType: TransactionType) {
                      createCategory(request: {name: $name, transactionType: $transactionType}) {
                        id
                        name
                        transactionType
                      }
                    }
                     """;
            tester.document(createDocument)
                    .variable("name", categoryNames[i])
                    .variable("transactionType", transactionTypes[i])
                    .execute();
        }


        String queryDocument = """
                query getAllCategories {
                    getAllCategories {
                     id
                     name
                     transactionType
                    }
                  }
                """;

        List<CategoryResponse> responseList = tester.document(queryDocument)
                .execute()
                .path("getAllCategories")
                .entityList(CategoryResponse.class)
                .get();

        Assertions.assertEquals(categoryNames.length, responseList.size());

        for (int i = 0; i < responseList.size(); i++) {
            Assertions.assertEquals((i + 1), responseList.get(i).getId());
            Assertions.assertEquals(categoryNames[i], responseList.get(i).getName());
            Assertions.assertEquals(transactionTypes[i], responseList.get(i).getTransactionType());
        }

    }
}
