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

public class ChangeUserDataTest {

    private User user = UserGenerator.getDefault();
    private UserClient userClient = new UserClient();
    private Response response;

    private int statusCode;
    private int statusCodeLoginError;
    private int statusCodeEmailError;

    private boolean expected;
    private boolean expectedError;

    private String loginErrorMessage;
    private String emailErrorMessage;
    private String accessToken;
    private String newName = "denis70";
    private String newEmail = "denis70@yandex.ru";
    private String newPassword = "147852369";

    @Before
    public void setUp() {
        response = userClient.createUser(user);
        accessToken = response.then().extract().path("accessToken");
        statusCode = 200;
        statusCodeLoginError = 401;
        statusCodeEmailError = 403;
        expected = true;
        expectedError = false;
        loginErrorMessage = "You should be authorised";
        emailErrorMessage = "User with such email already exists";

    }

    @Test
    @DisplayName("Изменить имя авторизованного пользователя")
    public  void changeAuthorizedUserName() {
        user.setName(newName);
        Response response1 = userClient.changeUser(accessToken, user);
        response1.then().assertThat().statusCode(statusCode)
                .and().body("success", equalTo(expected));
        Response response2 = userClient.findUser(accessToken);
        response2.then().assertThat().statusCode(statusCode)
                .and().body("user.name", equalTo(newName));
    }

    @Test
    @DisplayName("Изменить адрес электронной почты для авторизованного пользователя")
    public void  changeAuthorizedUserEmail(){
        user.setEmail(newEmail);
        Response response1 = userClient.changeUser(accessToken, user);
        response1.then().assertThat().statusCode(statusCode)
                .and().body("success", equalTo(expected));
        Response response2 = userClient.findUser(accessToken);
        response2.then().assertThat().statusCode(statusCode)
                .and().body("user.email", equalTo(newEmail));
    }

    @Test
    @DisplayName("изменить пароль для авторизованного пользователя")
    public  void changeAuthorizedUserPassword(){
        user.setPassword(newPassword);
        Response response1 = userClient.changeUser(accessToken, user);
        response1.then().assertThat().statusCode(statusCode)
                .and().body("success", equalTo(expected));
        Response response2 = userClient.loginUser(Credentials.from(user));
        response2.then().assertThat().statusCode(statusCode).and().body("success", equalTo(expected));
    }

    @Test
    @DisplayName("Изменить адрес электронной почты")
    public void  changeAuthorizedUserEmailForBusyOne(){
        Response response1 = userClient.findUser(accessToken);
        String emailDefault = response1.then().extract().path("user.email");
        user =  UserGenerator.getAnother();
        Response response2 = userClient.createUser(user);
        String accessToken = response2.then().extract().path("accessToken");
        user.setEmail(emailDefault);
        Response response3 = userClient.changeUser(accessToken, user);
        response3.then().assertThat().statusCode(statusCodeEmailError)
                .and().body("success", equalTo(expectedError))
                .and().body("message", equalTo(emailErrorMessage));
    }

    @Test
    @DisplayName("Изменить имя для неавторизованного пользователя")
    public  void changeNotAuthorizedUserName(){
        user.setName(newName);
        String accessToken = "";
        Response response1 = userClient.changeUser(accessToken, user);
        response1.then().assertThat().statusCode(statusCodeLoginError)
                .and().body("message", equalTo(loginErrorMessage));
    }

    @Test
    @DisplayName("Изменить пароль для неавторизованного пользователя")
    public  void changeNotAuthorizedUserPassword(){
        user.setPassword(newPassword);
        String accessToken = "";
        Response response1 = userClient.changeUser(accessToken, user);
        response1.then().assertThat().statusCode(statusCodeLoginError)
                .and().body("message", equalTo(loginErrorMessage));
    }

    @Test
    @DisplayName("Изменить адрес электронной почты для неавторизованного пользователя")
    public void  changeNotAuthorizedUserEmail(){
        user.setEmail(newEmail);
        String accessToken = "";
        Response response1 = userClient.changeUser(accessToken, user);
        response1.then().assertThat().statusCode(statusCodeLoginError)
                .and().body("message", equalTo(loginErrorMessage));
    }


    @After
    public void delete() {
        userClient.deleteUser(accessToken);
    }
}
