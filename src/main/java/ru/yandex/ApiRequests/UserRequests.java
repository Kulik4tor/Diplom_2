package ru.yandex.ApiRequests;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.ApiData.UserData;

import static io.restassured.RestAssured.given;

public class UserRequests extends Api {

    @Step("Регистрация пользователя")
    public Response userRegistration(UserData user) {
        return given()
                .spec(apiSpecification())
                .and()
                .body(user)
                .post("/auth/register");
    }

    @Step("Получение токена")
    public String getUserAccessToken(UserData user) {
        String auToken = "";
        Response response = given()
                .spec(apiSpecification())
                .and()
                .body(user)
                .post("/auth/login");

        if (response.statusCode() == 200) {
            auToken = response.body().path("accessToken");
        }
        return auToken;
    }

    @Step("Удаление пользователя")
    public void deleteRegisteredUser(String auToken) {
        given()
                .headers("Authorization", auToken)
                .spec(apiSpecification())
                .delete("auth/user")
                .then()
                .statusCode(202);
    }

    @Step("Авторизация пользователя")
    public Response authorizationRegisteredUser(UserData user) {
        return given()
                .spec(apiSpecification())
                .and()
                .body(user.getLoginInfo())
                .post("/auth/login");
    }

    @Step("Редактирование пользователя с авторизацией")
    public Response patchUserDataWithAuthorization(String auToken, UserData user) {
        return given()
                .headers("Authorization", auToken)
                .spec(apiSpecification())
                .and()
                .body(user)
                .patch("/auth/user");
    }

    @Step("Редактирование пользователя с авторизацией")
    public Response patchUserDataWithoutAuthorization(UserData user) {
        return given()
                .spec(apiSpecification())
                .and()
                .body(user)
                .patch("/auth/user");
    }
}
