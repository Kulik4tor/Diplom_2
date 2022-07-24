package ru.yandex.UserTests;

import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.ApiData.UserData;
import ru.yandex.ApiRequests.UserRequests;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class PatchUserDataTest {
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
    public void updateUserDataWithAuthorization() {
        userData.setPassword("passwordNew");
        userData.setEmail("emailnew@gmail.com");
        userData.setName("newName");
        Response response = userRequests.patchUserDataWithAuthorization(acToken, userData);
        response.then().statusCode(SC_OK)
                .and().assertThat().body("success", equalTo(true))
                .body("user.email", equalTo(userData.getEmail()))
                .body("user.name", equalTo(userData.getName()));
        //Тут смотрю что пароль тоже изменился, по-хорошему отдельный тест надо, но пусть будет тут
        userRequests.authorizationRegisteredUser(userData.getLoginInfo())
                .then().assertThat().body("success", equalTo(true));
    }

    @Test
    public void updateUserDataWithoutAuthorization() {
        userData.setPassword("passwordNew");
        userData.setEmail("emailnew@gmail.com");
        userData.setName("newName");
        Response response = userRequests.patchUserDataWithoutAuthorization(userData);
        response.then().statusCode(SC_UNAUTHORIZED)
                .and().assertThat().body("message", equalTo("You should be authorised"));
    }

    @After
    public void tearDown() {
        userRequests.deleteRegisteredUser(acToken);
    }
}
