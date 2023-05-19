package org.example.order;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.example.Order;
import org.example.User;
import org.example.clients.OrderClient;
import org.example.clients.UserClient;
import org.example.generators.OrderGenerator;
import org.example.generators.UserGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotEquals;


public class CreateInvalidOrderTest {
    private User user = UserGenerator.getDefault();
    private UserClient userClient = new UserClient();
    private OrderClient orderClient = new OrderClient();

    private Order order;

    private int statusCode;
    private int invalidIngredientStatusCode;
    private int nullIngredientStatusCode;

    private boolean expected;
    private boolean expectedError;

    private String nullIngredientErrorMessage;
    private String accessToken;

    @Before
    public void setUp() {
        Response responseUser = userClient.createUser(user);
        accessToken = responseUser.then().extract().path("accessToken");
        Response responseIngredients = orderClient.getIngredients();
        List<String> jsonResponse = responseIngredients.then().extract().body().jsonPath().getList("data._id");
        order = OrderGenerator.getDefault(jsonResponse);
        statusCode = 200;
        invalidIngredientStatusCode = 500;
        nullIngredientStatusCode = 400;
        expected = true;
        expectedError = false;
        nullIngredientErrorMessage = "Ingredient ids must be provided";
    }

    @Test
    @DisplayName("Создать заказ для неавторизованного пользователя")
    @Description("Тест не пройдет, согласно документам невозможно создать заказ без авторизации, но на практике это возможно")
    public void createOrderForNotAuthorizedUser() {
        String accessToken = "";
        Response responseOrder = orderClient.createNewOrder(accessToken, order);
        int sC = responseOrder.then().extract().statusCode();
        assertNotEquals("Ошибка - заказ может быть создан для неавторизованного пользователя! ", sC, statusCode);
    }

    @Test
    @DisplayName("Опубликовать недопустимые ингредиенты")
    @Description("Проверяем только код состояния, ответ на запрос не содержит тела")
    public void createOrderWithInvalidIngredients() {
        order = OrderGenerator.getWithInvalidIngredients();
        Response responseOrder = orderClient.createNewOrder(accessToken, order);
        responseOrder.then().assertThat().statusCode(invalidIngredientStatusCode);
    }

    @Test
    @DisplayName("Заказ без ингредиентов")
    public void createOrderWithoutIngredients() {
        order.ingredients.clear();
        Response responseOrder = orderClient.createNewOrder(accessToken, order);
        responseOrder.then().assertThat().statusCode(nullIngredientStatusCode)
                .and().body("message", equalTo(nullIngredientErrorMessage));
    }

    @After
    public void delete() {
        userClient.deleteUser(accessToken);
    }
}

