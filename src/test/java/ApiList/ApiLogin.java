package ApiList;

import io.restassured.response.Response;
import org.example.LoginCourier;

import static io.restassured.RestAssured.given;

public class ApiLogin {

    private static final String URL = "https://qa-scooter.praktikum-services.ru";
    private static final String LOGIN_API = "/api/v1/courier/login";
    private static final String CONTENT_TYPE = "application/json";

    public static Response getLogin(LoginCourier LoginCourier) {
        return given().baseUri(URL)
                .header("Content-type", CONTENT_TYPE)
                .body(LoginCourier)
                .when()
                .post(LOGIN_API);
    }

    public static Response getLogin(LoginCourier LoginCourier, String post) {
        return given().baseUri(URL)
                .header("Content-type", CONTENT_TYPE)
                .body(LoginCourier)
                .when()
                .post(post);
    }

}
