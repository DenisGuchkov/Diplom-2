package org.example.user;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.example.Credentials;
import org.example.User;
import org.example.clients.UserClient;
import org.example.generators.UserGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;


public class LoginUserTest {
    private User user;

    private UserClient userClient;

    private int statusCode;
    private int statusCodeError;

    private boolean expected;
    private boolean expectedError;

    private String errorMessage;
    private String accessToken;
    private String newPassword;
    private String newEmail;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getDefault();
        Response response = userClient.createUser(user);
        accessToken = response.then().extract().path("accessToken");
        statusCode = 200;
        statusCodeError = 401;
        expected = true;
        expectedError = false;
        errorMessage = "email or password are incorrect";
        newPassword = "Si";
        newEmail = "Sb";

    }
    @Test
    @DisplayName("Авторизация пользователя")
    public void loginUser() {
        Response response1 = userClient.loginUser(Credentials.from(user));
        response1.then().assertThat().statusCode(statusCode).and().body("success", equalTo(expected));
    }

    @Test
    @DisplayName("Авторизация с неверным паролем")
    public  void authorizationWithInvalidPassword(){
        User user =  UserGenerator.authorization();
        user.setPassword(newPassword);
        Response response1 = userClient.loginUser(Credentials.from(user));
        response1.then().assertThat().statusCode(statusCodeError).and().body("message", equalTo(errorMessage));
    }
    @Test
    @DisplayName("Авторизация с неверным адресом электронной почты")
    public void  authorizationWithInvalidEmail(){
        User user =  UserGenerator.authorization();
        user.setEmail(newEmail);
        Response response1 = userClient.loginUser(Credentials.from(user));
        response1.then().assertThat().statusCode(statusCodeError).and().body("message", equalTo(errorMessage));
    }

    @Test
    @DisplayName("Авторизация с неверным паролем и адресом электронной почты")
    public void  authorizationWithInvalidLogin(){
        User user =  UserGenerator.authorization();
        user.setEmail(newEmail);
        user.setPassword(newPassword);
        Response response1 = userClient.loginUser(Credentials.from(user));
        response1.then().assertThat().statusCode(statusCodeError).and().body("message", equalTo(errorMessage));
    }


    @After
    public void delete() {
        userClient.deleteUser(accessToken);
    }
}
