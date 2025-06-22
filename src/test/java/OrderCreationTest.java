import io.restassured.response.Response;
import org.example.CreatingOrder;
import org.example.TestDataFactory;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import api.list.ApiOrder;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(Parameterized.class)
public class OrderCreationTest {
    private final List<String> colorOptions;
    private Integer trackNumber;

    public OrderCreationTest(List<String> colorOptions) {
        this.colorOptions = colorOptions;
    }

    @Parameterized.Parameters(name = "Color options: {0}")
    public static Collection<Object[]> testData() {
        return Arrays.asList(new Object[][]{
                {Arrays.asList("BLACK")},
                {Arrays.asList("GREY")},
                {Arrays.asList("BLACK", "GREY")},
                {null} // тест без указания цвета
        });
    }

    @Test
    public void shouldCreateOrderWithDifferentColorOptions() {
        CreatingOrder order = TestDataFactory.createNewOrder(colorOptions);

        Response response = ApiOrder.createOrder(order);

        response.then()
                .statusCode(201)
                .body("track", notNullValue());

        trackNumber = response.jsonPath().getInt("track");
        assertThat(trackNumber, greaterThan(0));
    }

    @After
    public void tearDown() {
        if (trackNumber != null) {
            try {
                // Отменяем заказ
                Response cancelResponse = ApiOrder.cancelOrder(trackNumber);

                int statusCode = cancelResponse.getStatusCode();
                assertThat(statusCode, anyOf(is(200), is(400)));

                if (statusCode != 200) {
                    System.out.println("Order cancellation returned status: " + statusCode);
                }
            } catch (Exception e) {

            }
        }
    }
}


