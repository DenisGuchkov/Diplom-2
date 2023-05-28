package org.example.clients;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.example.Order;

import static io.restassured.RestAssured.given;

public class OrderClient extends Client {

    public static final String
            ORDER = "/api/orders",
            INGREDIENTS = "/api/ingredients";

    @Step("Получить ингредиенты")
    public Response getIngredients() {
        Response response =
                given()
                        .spec(getSpec())
                        .when()
                        .get(INGREDIENTS);
        return response;
    }

    @Step("Создать новый заказ")
    public Response createNewOrder(String accessToken, Order ingredients) {
        Response response =
                given()
                        .spec(getSpec())
                        .header("Authorization", accessToken)
                        .body(ingredients)
                        .when()
                        .post(ORDER);
        return response;
    }

    @Step("Получить заказ")
    public Response findOrder(String accessToken) {
        Response response =
                given()
                        .header("Authorization", accessToken)
                        .spec(getSpec())
                        .when()
                        .get(ORDER);
        return response;
    }


}
