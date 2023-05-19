package org.example.clients;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.example.Credentials;
import org.example.User;

import static io.restassured.RestAssured.given;

public class UserClient extends Client {

    public static final String
            CREATE_USER = "/api/auth/register",
            LOGIN_USER = "/api/auth/login",
            DATA_USER = "/api/auth/user";


    public static final int STATUS_CODE = 202;

    @Step("Создать нового пользователя")
    public Response createUser(User user) {
        return given()
                .spec(getSpec())
                .body(user)
                .when()
                .post(CREATE_USER);
    }
    @Step("Авторизация пользователя")
    public Response loginUser(Credentials user) {
        return given()
                .spec(getSpec())
                .body(user)
                .when()
                .post(LOGIN_USER);
    }

    @Step("Найти пользователя")
    public Response findUser(String accessToken) {
        Response response =
                given()
                        .header("Authorization", accessToken)
                        .spec(getSpec())
                        .when()
                        .get(DATA_USER);
        return response;
    }

    @Step("Сменить пользователя")
    public Response changeUser(String accessToken, User user ) {
        return  given()
                .header("Authorization", accessToken)
                .spec(getSpec())
                .body(user)
                .when()
                .patch(DATA_USER);
    }

    @Step("Удалить пользователя")
    public void deleteUser(String accessToken) {
        if (accessToken == null) {
            return;
        }
        given()
                .header("Authorization", accessToken)
                .spec(getSpec())
                .when()
                .delete(DATA_USER)
                .then()
                .statusCode(STATUS_CODE);
    }

}
