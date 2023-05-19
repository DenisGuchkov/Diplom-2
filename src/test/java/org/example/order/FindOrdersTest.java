package org.example.order;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.example.Credentials;
import org.example.User;
import org.example.clients.OrderClient;
import org.example.clients.UserClient;
import org.example.generators.UserGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class FindOrdersTest {
    private User user = UserGenerator.getDefault();

    private UserClient userClient = new UserClient();
    private OrderClient orderClient = new OrderClient();

    private String accessToken;

    @Before
    public void setUp() {
        Response response = userClient.createUser(user);
        accessToken = response.then().extract().path("accessToken");
    }

    @Test
    @DisplayName("Найти заказ авторизованного пользователя")
    public void findOrderToAuthorizedUser() {
        int statusCodeSuccess = 200;
        userClient.loginUser(Credentials.from(user));
        Response responseOrder = orderClient.findOrder(accessToken);
        responseOrder.then().statusCode(statusCodeSuccess).and().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Найти заказ для неавторизованного пользователя")
    public void findOrderToNotAuthorizedUser() {
        int statusCodeError = 401;
        String accessToken = "";
        Response responseOrder = orderClient.findOrder(accessToken);
        responseOrder.then().statusCode(statusCodeError).and().body("message", equalTo("You should be authorised"));
    }


    @After
    public void delete() {
        userClient.deleteUser(accessToken);
    }
}
