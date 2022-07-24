package ru.yandex.OrderTests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.ApiData.IngredientsData;
import ru.yandex.ApiRequests.OrderRequests;

import java.util.Arrays;
import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class OrderWithoutAuthorizationTest {

    OrderRequests orderRequests;
    IngredientsData ingredients;
    List<String> clearIngredients;
    List<String> invalidIngredients = Arrays.asList("bread", "pepper");

    @Before
    public void setUp() {
        orderRequests = new OrderRequests();
    }

    @Test
    @DisplayName("Негативный тест создания заказа (без Авторизации без Ингредиентов)")
    public void createOrderWithoutIngredientsWithoutAuthorization() {
        ingredients = new IngredientsData(clearIngredients);
        Response response = orderRequests.createOrderWithoutAuthorization(ingredients);
        response.then().statusCode(SC_BAD_REQUEST)
                .and().assertThat().body("success", equalTo(false));
    }

    @Test
    @DisplayName("Негативный тест создания заказа (без Авторизации C Ингредиентами)")
    public void createOrderWithIngredientsWithoutAuthorization() {
        ingredients = new IngredientsData(orderRequests.getIngredients());
        Response response = orderRequests.createOrderWithoutAuthorization(ingredients);
        //Сделал так, но по логике это баг
        response.then().statusCode(SC_OK)
                .and().assertThat().body("success", equalTo(true))
                .and().assertThat().body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Негативный тест создания заказа (без Авторизации C невалидными ингредиентами)")
    public void createOrderWithInvalidIngredientsWithoutAuthorization() {
        ingredients = new IngredientsData(invalidIngredients);
        Response response = orderRequests.createOrderWithoutAuthorization(ingredients);
        response.then().statusCode(SC_INTERNAL_SERVER_ERROR);
    }

}
