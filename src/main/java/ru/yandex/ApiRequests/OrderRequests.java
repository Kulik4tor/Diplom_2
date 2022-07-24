package ru.yandex.ApiRequests;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.ApiData.IngredientsData;

import java.util.List;

import static io.restassured.RestAssured.given;

public class OrderRequests extends Api {

    @Step("Получение получение ингредиентов")
    public List<String> getIngredients() {
        Response response = given()
                .spec(apiSpecification())
                .get("/ingredients");
        return response.body().path("data._id");
    }


    @Step("Создание заказа")
    public Response createOrderWithAuthorization(IngredientsData ingredients, String acToken) {
        return given()
                .spec(apiSpecification())
                .headers("Authorization", acToken)
                .body(ingredients)
                .post("/orders");
    }

    @Step("Создать заказ без авторизации")
    public Response createOrderWithoutAuthorization(IngredientsData ingredients) {
        return given()
                .spec(apiSpecification())
                .body(ingredients)
                .post("/orders");
    }

    @Step("Получить список заказов пользователя")
    public Response ordersUserAuthorization(String acToken) {
        return given()
                .headers("Authorization", acToken)
                .spec(apiSpecification())
                .get("/orders");
    }

    @Step("Получить список заказов пользователя без авторизации")
    public Response ordersUserWithoutToken() {
        return given()
                .spec(apiSpecification())
                .get("/orders");
    }
}
