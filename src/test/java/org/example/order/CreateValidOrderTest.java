package org.example.order;

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

public class CreateValidOrderTest {
    private User user = UserGenerator.getDefault();

    private UserClient userClient = new UserClient();
    private OrderClient orderClient = new OrderClient();

    private Order order;

    private String accessToken;


    @Before
    public void setUp() {
        Response response = userClient.createUser(user);
        accessToken = response.then().extract().path("accessToken");
        Response responseGetIngredients = orderClient.getIngredients();
        List<String> jsonResponse =  responseGetIngredients.then().extract().body().jsonPath().getList("data._id");
        order = OrderGenerator.getDefault(jsonResponse);
    }

    @Test
    @DisplayName("Создать действительный заказ для авторизованного пользователя")
    public void createValidOrder() {
        int statusCodeSuccess = 200;
        Response responseOrder = orderClient.createNewOrder(accessToken, order);
        responseOrder.then().assertThat().statusCode(statusCodeSuccess)
                .and().body("success", equalTo(true));
    }

    @After
    public void delete() {
        userClient.deleteUser(accessToken);
    }
}
