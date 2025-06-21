package ApiList;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.example.CreatingOrder;

import static io.restassured.RestAssured.given;

public class ApiOrder {
    private static final String BASE_URI = "https://qa-scooter.praktikum-services.ru";
    private static final String ORDERS_API = "/api/v1/orders";

    public static Response createOrder(CreatingOrder order) {
        return given()
                .baseUri(BASE_URI)
                .contentType(ContentType.JSON)
                .body(order)
                .when()
                .post(ORDERS_API);
    }

    public static Response cancelOrder(int trackId) {
        return given()
                .baseUri(BASE_URI)
                .contentType(ContentType.JSON)
                .body(String.format("{\"track\": %d}", trackId))
                .when()
                .put(ORDERS_API + "/cancel");
    }

    public static Response getAllOrders() {
        return given()
                .baseUri("https://qa-scooter.praktikum-services.ru")
                .contentType(ContentType.JSON)
                .when()
                .get(ORDERS_API);
    }
    public static void deleteOrderById(String orderId) {
        given().baseUri("https://qa-scooter.praktikum-services.ru")
                .header("Content-type", "application/json")
                .body("{\"orderId\": \"" + orderId + "\"}")
                .when()
                .put("/api/v1/orders/cancel");
    }

    public static Response getOrdersByStation(String station) {
        return given()
                .baseUri("https://qa-scooter.praktikum-services.ru")
                .contentType(ContentType.JSON)
                .queryParam("nearestStation", station)
                .when()
                .get(ORDERS_API);
    }
    public static Response getOrderInfo(int trackId) {
        return given()
                .baseUri(BASE_URI)
                .queryParam("t", trackId)
                .when()
                .get(ORDERS_API + "/track");
    }
}

