import ApiList.ApiCourier;
import io.restassured.response.Response;
import org.example.CreatingCourier;
import org.example.TestDataFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CourierTest {
    private CreatingCourier validCourier;
    private String registeredCourierId;

    @Before
    public void initializeTestData() {
        validCourier = TestDataFactory.getStandardDeliveryPerson();
    }

    @Test
    public void whenRegisterValidCourier_thenSuccess() {
        Response registrationResponse = ApiCourier.registerCourier(validCourier);

        registrationResponse.then()
                .assertThat()
                .statusCode(201)
                .body("ok", is(true));

        registeredCourierId = fetchCourierId(validCourier.getLogin(), validCourier.getPassword());
    }

    @Test
    public void whenRegisterDuplicateCourier_thenConflict() {
        // Первичная регистрация
        ApiCourier.registerCourier(validCourier);
        registeredCourierId = fetchCourierId(validCourier.getLogin(), validCourier.getPassword());

        // Попытка дублирования
        Response duplicateResponse = ApiCourier.registerCourier(validCourier);

        duplicateResponse.then()
                .assertThat()
                .statusCode(409)
                .body("message", containsString("Этот логин уже используется"));
    }

    @Test
    public void whenRegisterWithoutCredentials_thenBadRequest() {
        CreatingCourier invalidCourier = new CreatingCourier();
        invalidCourier.setFirstName("Только имя");

        Response response = ApiCourier.registerCourier(invalidCourier);

        response.then()
                .assertThat()
                .statusCode(400)
                .body("message", notNullValue());
    }

    @Test
    public void whenRegisterWithoutPassword_thenRejected() {
        CreatingCourier noPasswordCourier = new CreatingCourier();
        noPasswordCourier.setLogin("new_login");
        noPasswordCourier.setFirstName("Имя без пароля");

        Response response = ApiCourier.registerCourier(noPasswordCourier);

        response.then()
                .assertThat()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    public void whenRegisterWithoutName_thenAccepted() {
        CreatingCourier namelessCourier = new CreatingCourier();
        namelessCourier.setLogin("nameless_login");
        namelessCourier.setPassword("secure123");

        Response response = ApiCourier.registerCourier(namelessCourier);

        response.then()
                .assertThat()
                .statusCode(201)
                .body("ok", is(true));

        registeredCourierId = fetchCourierId(namelessCourier.getLogin(), namelessCourier.getPassword());
    }

    @After
    public void cleanup() {
        if (registeredCourierId != null) {
            removeTestCourier(registeredCourierId);
        }
    }

    private String fetchCourierId(String login, String password) {
        Response authResponse = ApiCourier.authenticateCourier(login, password);
        return authResponse.jsonPath().getString("id");
    }

    private void removeTestCourier(String id) {
        given().baseUri(ApiCourier.ConstantsURL.URL)
                .when()
                .delete("/api/v1/courier/" + id)
                .then()
                .statusCode(200);
    }
}
