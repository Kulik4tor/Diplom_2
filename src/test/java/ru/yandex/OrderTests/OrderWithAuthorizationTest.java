package ru.yandex.OrderTests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.ApiData.IngredientsData;
import ru.yandex.ApiData.UserData;
import ru.yandex.ApiRequests.OrderRequests;
import ru.yandex.ApiRequests.UserRequests;

import java.util.Arrays;
import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class OrderWithAuthorizationTest {

    UserRequests userRequests;
    OrderRequests orderRequests;
    UserData userData;
    String acToken;
    IngredientsData ingredients;
    List<String> clearIngredients;
    List<String> invalidIngredients = Arrays.asList("Potato", "meat");

    @Before
    public void setUp() {
        orderRequests = new OrderRequests();
        userRequests = new UserRequests();
        userData = new UserData("Кирилл", "emailKirill@gmail.ru", "passwordKirill");
        Response response = userRequests.userRegistration(userData);
        acToken = response.body().path("accessToken");

    }

    @Test
    @DisplayName("Позитивный тест создания заказа (Авторизация + Ингредиенты)")
    public void createOrderWithCorrectIngredientsAndAuthorization() {
        ingredients = new IngredientsData(orderRequests.getIngredients());
        Response response = orderRequests.createOrderWithAuthorization(ingredients, acToken);
        response.then().statusCode(SC_OK)
                .and().assertThat().body("success", equalTo(true))
                .and().assertThat().body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Негативный тест создания заказа (Авторизация без Ингредиентов)")
    public void createOrderWithoutIngredientsWithAuthorization() {
        ingredients = new IngredientsData(clearIngredients);
        Response response = orderRequests.createOrderWithAuthorization(ingredients, acToken);
        response.then().statusCode(SC_BAD_REQUEST)
                .and().assertThat().body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Негативный тест создания заказа (Авторизация c невалидными ингредиентов)")
    public void createOrderWithInvalidIngredientsWithAuthorization() {
        ingredients = new IngredientsData(invalidIngredients);
        Response response = orderRequests.createOrderWithAuthorization(ingredients, acToken);
        response.then().statusCode(SC_INTERNAL_SERVER_ERROR);
    }


    @After
    public void tearDown() {
        userRequests.deleteRegisteredUser(acToken);
    }

}
