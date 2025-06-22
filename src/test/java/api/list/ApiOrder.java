package api.list;

import io.qameta.allure.Step;
import io.qameta.allure.internal.shadowed.jackson.databind.node.JsonNodeFactory;
import io.qameta.allure.internal.shadowed.jackson.databind.node.ObjectNode;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.example.CreatingOrder;

import static io.restassured.RestAssured.given;

public class ApiOrder {
    private static final String BASE_URI = "https://qa-scooter.praktikum-services.ru";
    private static final String ORDERS_API = "/api/v1/orders";

    @Step("Создать новый заказ")
    public static Response createOrder(CreatingOrder order) {
        return given()
                .log().all()
                .baseUri(BASE_URI)
                .contentType(ContentType.JSON)
                .body(order)
                .when()
                .post(ORDERS_API);
    }

    @Step("Отменить заказ по trackId")
    public static Response cancelOrder(int trackId) {
        // Создаем объект для тела запроса
        ObjectNode requestBody = JsonNodeFactory.instance.objectNode()
                .put("track", trackId);

        return given()
                .log().all()
                .baseUri(BASE_URI)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .put(ORDERS_API + "/cancel");
    }

    @Step("Получить список всех заказов")
    public static Response getAllOrders() {
        return given()
                .log().all()
                .baseUri(BASE_URI)
                .contentType(ContentType.JSON)
                .when()
                .get(ORDERS_API);
    }

    @Step("Удалить заказ по ID")
    public static void deleteOrderById(String orderId) {
        given()
                .log().all()
                .baseUri(BASE_URI)
                .contentType(ContentType.JSON)
                .pathParam("id", orderId)
                .when()
                .delete(ORDERS_API + "/{id}");
    }

    @Step("Получить заказы по станции метро")
    public static Response getOrdersByStation(String station) {
        return given()
                .log().all()
                .baseUri(BASE_URI)
                .contentType(ContentType.JSON)
                .queryParam("nearestStation", station)
                .when()
                .get(ORDERS_API);
    }

    @Step("Получить информацию о заказе по trackId")
    public static Response getOrderInfo(int trackId) {
        return given()
                .log().all()
                .baseUri(BASE_URI)
                .queryParam("t", trackId)
                .when()
                .get(ORDERS_API + "/track");
    }
}

