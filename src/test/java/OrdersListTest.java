import ApiList.ApiOrder;
import io.restassured.response.Response;
import org.example.CreatingOrder;
import org.example.TestDataFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.*;

public class OrdersListTest {

    private CreatingOrder testOrder;
    private int trackId;

    @Before
    public void setUp() {
        // Создаем тестовый заказ перед каждым тестом
        testOrder = TestDataFactory.createNewOrder();
        Response createResponse = ApiOrder.createOrder(testOrder);
        trackId = createResponse.jsonPath().getInt("track");
    }

    @After
    public void tearDown() {
        // Отменяем тестовый заказ после каждого теста
        if (trackId != 0) {
            ApiOrder.cancelOrder(trackId);
        }
    }

    @Test
    public void testGetAllOrdersReturnsListOfOrders() {
        // Получаем список всех заказов
        Response response = ApiOrder.getAllOrders();

        // Проверяем, что ответ содержит список заказов
        response.then()
                .assertThat()
                .statusCode(200)
                .body("orders", notNullValue())
                .body("orders", instanceOf(List.class))
                .body("orders.size()", greaterThan(0));
    }

    @Test
    public void testOrderListContainsRequiredFields() {
        // Получаем список всех заказов
        Response response = ApiOrder.getAllOrders();

        // Проверяем, что каждый заказ содержит обязательные поля
        response.then()
                .assertThat()
                .statusCode(200)
                .body("orders[0].id", notNullValue())
                .body("orders[0].firstName", notNullValue())
                .body("orders[0].lastName", notNullValue())
                .body("orders[0].address", notNullValue())
                .body("orders[0].metroStation", notNullValue())
                .body("orders[0].phone", notNullValue())
                .body("orders[0].rentTime", notNullValue())
                .body("orders[0].deliveryDate", notNullValue())
                .body("orders[0].track", notNullValue());
    }

    @Test
    public void testGetOrdersByStationReturnsFilteredList() {
        // Получаем заказы для конкретной станции
        Response response = ApiOrder.getOrdersByStation(TestDataFactory.NEAREST_STATION);

        // Проверяем, что ответ содержит список заказов
        response.then()
                .assertThat()
                .statusCode(200)
                .body("orders", notNullValue())
                .body("orders", instanceOf(List.class))
                .body("orders.metroStation", everyItem(equalTo(TestDataFactory.NEAREST_STATION)));
    }

    @Test
    public void testOrderInfoContainsCorrectData() {
        // Получаем информацию о конкретном заказе
        Response orderInfoResponse = ApiOrder.getOrderInfo(trackId);

        // Проверяем, что данные соответствуют тестовым данным
        orderInfoResponse.then()
                .assertThat()
                .statusCode(200)
                .body("order.firstName", equalTo(TestDataFactory.CUSTOMER_NAME))
                .body("order.lastName", equalTo(TestDataFactory.CUSTOMER_SURNAME))
                .body("order.address", equalTo(TestDataFactory.DELIVERY_ADDRESS))
                .body("order.metroStation", equalTo(TestDataFactory.NEAREST_STATION))
                .body("order.phone", equalTo(TestDataFactory.CONTACT_NUMBER))
                .body("order.comment", equalTo(TestDataFactory.ORDER_NOTES));
    }

    @Test
    public void testCanceledOrderNotInActiveList() {
        ApiOrder.cancelOrder(trackId);

        // Получаем список активных заказов
        Response activeOrdersResponse = ApiOrder.getAllOrders();

        // Проверяем, что отмененный заказ отсутствует в списке
        activeOrdersResponse.then()
                .assertThat()
                .statusCode(200)
                .body("orders.track", not(hasItem(trackId)));
    }
}
