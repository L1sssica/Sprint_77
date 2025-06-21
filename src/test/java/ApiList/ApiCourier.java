package ApiList;

import io.restassured.response.Response;
import org.example.CreatingCourier;
import static io.restassured.RestAssured.given;

public class ApiCourier {

        public class ConstantsURL {
            public static final String URL = "https://qa-scooter.praktikum-services.ru";
        }

        public static Response registerCourier(CreatingCourier courierData) {
            return given().baseUri(ConstantsURL.URL)
                    .header("Content-type", "application/json")
                    .body(courierData)
                    .when()
                    .post("/api/v1/courier");
        }

        public static Response authenticateCourier(String login, String password) {
            return given().baseUri(ConstantsURL.URL)
                    .header("Content-type", "application/json")
                    .body(String.format("{\"login\":\"%s\",\"password\":\"%s\"}", login, password))
                    .when()
                    .post("/api/v1/courier/login");
        }

        public static Response deleteCourier(String courierId) {
            return given().baseUri(ConstantsURL.URL)
                    .when()
                    .delete("/api/v1/courier/" + courierId);
        }
    }