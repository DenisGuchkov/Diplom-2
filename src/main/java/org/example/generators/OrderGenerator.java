package org.example.generators;

import com.github.javafaker.Faker;
import io.qameta.allure.Step;
import org.example.Order;

import java.util.ArrayList;
import java.util.List;


public class OrderGenerator {
    private static Faker faker = new Faker();

    public static List<String> ingredients = new ArrayList<>();

    public  static List<String> getIngredients(List<String> jsonResponse) {
        int randomInt = faker.number().numberBetween(0, jsonResponse.size());

        for (int i = 0; i<randomInt; i++){
            ingredients.add(jsonResponse.get(faker.number().numberBetween(0, jsonResponse.size())));
        }
        return ingredients;
    };

    public  static List<String> getInvalidIngredients(){
        ingredients.add(faker.bothify("?#####?147wsd"));
        return ingredients;
    };
    @Step("Получить дату действия заказа")
    public static Order getDefault(List<String> jsonResponse){
        return new Order(getIngredients( jsonResponse));
    }

    @Step("Получить неверную дату заказа")
    public static Order getWithInvalidIngredients(){
        return new Order(getInvalidIngredients());
    }
}

