package ru.yandex.UserTests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.ApiData.UserData;
import ru.yandex.ApiRequests.UserRequests;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class AuthorizationUserTest {
    UserRequests userRequests;
    UserData userData;
    String acToken;

    @Before
    public void setUp() {
        userRequests = new UserRequests();
        userData = new UserData("Кирилл", "emailKirill@gmail.ru", "passwordKirill");
        Response response = userRequests.userRegistration(userData);
        acToken = response.body().path("accessToken");
    }

    @Test
    @DisplayName("Позитивный тест авторизации пользователя")
    public void authorizationUserWithCorrectData() {
        Response response = userRequests.authorizationRegisteredUser(userData.getLoginInfo());
        response.then().statusCode(SC_OK)
                .and().assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Авторизации пользователя с некорректными данными")
    public void authorizationUserWithIncorrectData() {
        userData.setEmail("incorrect@gamal.ru");
        userData.setPassword("парольчик228");
        Response response = userRequests.authorizationRegisteredUser(userData.getLoginInfo());
        response.then().statusCode(SC_UNAUTHORIZED)
                .and().assertThat().body("success", equalTo(false));
    }

    @After
    public void tearDown() {
        userRequests.deleteRegisteredUser(acToken);
    }

}
