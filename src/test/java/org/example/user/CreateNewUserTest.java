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


public class CreateNewUserTest {

    private User user;
    private UserClient userClient;

    private int statusCode;
    private int statusCodeError;

    private boolean expected;

    private String userExistErrorMessage;
    private String notEnoughDataErrorMessage;


    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getDefault();
        statusCode = 200;
        statusCodeError = 403;
        expected = true;
        userExistErrorMessage = "User already exists";
        notEnoughDataErrorMessage = "Email, password and name are required fields";
    }

    @Test
    @DisplayName("Проверить сообщение - создание пользователя")
    public void checkCreatingUser() {
        Response response = userClient.createUser(user);
        try{
            Thread.sleep(3000);
        }
        catch(InterruptedException ie){
        }
        response.then().assertThat().statusCode(statusCode)
                .and().body("success", equalTo(expected));
    }

    @Test
    @DisplayName("Проверить сообщение - создание идентичного пользователя")
    public void checkCreatingIdenticalUser() {
        userClient.createUser(user);
        try{
            Thread.sleep(3000);
        }
        catch(InterruptedException ie){
        }
        Response response1 = userClient.createUser(user);

        response1.then().assertThat().statusCode(statusCodeError)
                .and().assertThat().body("message", equalTo(userExistErrorMessage));
    }

    @Test
    @DisplayName("Проверить сообщение о создании пользователя без имени")
    public void checkCreatingUserWithoutName() {
        user = UserGenerator.getWithoutName();
        Response response = userClient.createUser(user);
        try{
            Thread.sleep(3000);
        }
        catch(InterruptedException ie){
        }
        response.then().assertThat().statusCode(statusCodeError)
                .and()
                .assertThat().body("message", equalTo(notEnoughDataErrorMessage));
    }

    @Test
    @DisplayName("Проверить сообщение - создание пользователя без электронной почты")
    public void checkCreatingUserWithoutEmail() {
        user = UserGenerator.getWithoutEmail();
        Response response = userClient.createUser(user);
        try{
            Thread.sleep(3000);
        }
        catch(InterruptedException ie){
        }
        response.then().assertThat().statusCode(statusCodeError)
                .and()
                .assertThat().body("message", equalTo(notEnoughDataErrorMessage));
    }

    @Test
    @DisplayName("Проверить сообщение о создании пользователя без пароля")
    public void checkCreatingUserWithoutPassword() {
        user = UserGenerator.getWithoutPassword();
        Response response = userClient.createUser(user);
        try{
            Thread.sleep(3000);
        }
        catch(InterruptedException ie){
        }
        response.then().assertThat().statusCode(statusCodeError)
                .and()
                .assertThat().body("message", equalTo(notEnoughDataErrorMessage));
    }


    @After
    public void delete() {
        Response response = userClient.loginUser(Credentials.from(user));
        String accessToken = response.then().extract().path("accessToken");
        userClient.deleteUser(accessToken);
    }
}
