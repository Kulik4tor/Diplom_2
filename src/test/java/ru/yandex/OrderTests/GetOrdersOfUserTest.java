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

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.equalTo;

public class GetOrdersOfUserTest {

    UserRequests userRequests;
    OrderRequests orderRequests;
    UserData userData;
    String acToken;
    IngredientsData ingredients;
    int orderNumber;
    List<Integer> gottenOrder;

    @Before
    public void setUp() {
        orderRequests = new OrderRequests();
        userRequests = new UserRequests();
        userData = new UserData("Кирилл", "emailKirill@gmail.ru", "passwordKirill");
        Response response = userRequests.userRegistration(userData);
        acToken = response.body().path("accessToken");
        ingredients = new IngredientsData(orderRequests.getIngredients());
        //Тут я получай номер своего заказа и заношу его в лист. Это сделано для последующего сравнения. Мб можно было посимпатичнее сделать
        orderNumber = orderRequests.createOrderWithAuthorization(ingredients, acToken).path("order.number");
        gottenOrder = Arrays.asList(orderNumber);

    }

    @Test
    @DisplayName("Получение заказа авторизированного пользователя")
    public void getOrdersOfAuthorizedUser() {
        Response response = orderRequests.ordersUserAuthorization(acToken);
        response.then().statusCode(SC_OK)
                .and().assertThat().body("orders.number", equalTo(gottenOrder))
                .and().assertThat().body("success", equalTo(true));

    }

    @Test
    @DisplayName("Получение заказа не авторизированного пользователя")
    public void getOrdersOfUnauthorizedUser() {
        Response response = orderRequests.ordersUserWithoutToken();
        response.then().statusCode(SC_UNAUTHORIZED)
                .and().assertThat().body("success", equalTo(false));
    }

    @After
    public void tearDown() {
        userRequests.deleteRegisteredUser(acToken);
    }
}
