package api.list;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.example.CreatingCourier;
import org.example.LoginCourier;

import static io.restassured.RestAssured.given;

public class ApiCourier {

    public class ConstantsURL {
        public static final String URL = "https://qa-scooter.praktikum-services.ru";
    }

    @Step("Регистрация нового курьера")
    public static Response registerCourier(CreatingCourier courierData) {
        return given()
                .log().all()  // Логирование запроса
                .baseUri(ConstantsURL.URL)
                .header("Content-type", "application/json")
                .body(courierData)
                .when()
                .post("/api/v1/courier");
    }

    @Step("Аутентификация курьера")
    public static Response authenticateCourier(String login, String password) {
        LoginCourier loginData = new LoginCourier(login, password);

        return given()
                .log().all()  // Логирование запроса
                .baseUri(ConstantsURL.URL)
                .header("Content-type", "application/json")
                .body(loginData)
                .when()
                .post("/api/v1/courier/login");
    }

    @Step("Удаление курьера по ID")
    public static Response deleteCourier(String courierId) {
        return given()
                .log().all()  // Логирование запроса
                .baseUri(ConstantsURL.URL)
                .when()
                .delete("/api/v1/courier/" + courierId);
    }
}