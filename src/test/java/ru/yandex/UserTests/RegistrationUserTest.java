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

public class RegistrationUserTest {
    UserRequests userRequests;
    UserData userData;
    boolean needDeleteTestUser = false;

    @Before
    public void setUp() {
        userRequests = new UserRequests();
    }

    @Test
    @DisplayName("Позитивный тест регистрации пользователя")
    public void registrationUserWithCorrectData() {
        userData = new UserData("Кирилл", "emailKirill@gmail.ru", "passwordKirill");
        Response response = userRequests.userRegistration(userData);
        if (response.statusCode() == SC_OK) {
            needDeleteTestUser = true;
        }
        response.then().statusCode(SC_OK)
                .and().assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Регистрация пользователя где имя null")
    public void registrationUserWithoutName() {
        userData = new UserData(null, "emailKirill@gmail.ru", "passwordKirill");
        Response response = userRequests.userRegistration(userData);
        if (response.statusCode() == SC_OK) {
            needDeleteTestUser = true;
        }
        response.then().statusCode(SC_FORBIDDEN)
                .and().assertThat().body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Регистрация пользователя где почта null")
    public void registrationUserWithoutEmail() {
        userData = new UserData("Kirill", null, "passwordKirill");
        Response response = userRequests.userRegistration(userData);
        if (response.statusCode() == SC_OK) {
            needDeleteTestUser = true;
        }
        response.then().statusCode(SC_FORBIDDEN)
                .and().assertThat().body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Регистрация пользователя где пароль null")
    public void registrationUserWithoutPassword() {
        userData = new UserData("Kirill", "emailKirill@gmail.ru", null);
        Response response = userRequests.userRegistration(userData);
        if (response.statusCode() == SC_OK) {
            needDeleteTestUser = true;
        }
        response.then().statusCode(SC_FORBIDDEN)
                .and().assertThat().body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Регистрация уже существующего пользователя")
    public void registrationExistingUserWithCorrectData() {
        userData = new UserData("Кирилл", "emailKirill@gmail.ru", "passwordKirill");
        userRequests.userRegistration(userData);
        needDeleteTestUser = true;
        Response response = userRequests.userRegistration(userData);
        response.then().statusCode(SC_FORBIDDEN)
                .and().assertThat().body("message", equalTo("User already exists"));
    }


    @After
    public void tearDown() {
        if (needDeleteTestUser) {
            userRequests.deleteRegisteredUser(userRequests.getUserAccessToken(userData));
        }
    }
}
