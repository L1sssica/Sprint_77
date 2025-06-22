import api.list.ApiCourier;
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
    private String testLogin;
    private String testPassword;

    @Before
    public void initializeTestData() {
        validCourier = TestDataFactory.getStandardDeliveryPerson();
        testLogin = validCourier.getLogin();
        testPassword = validCourier.getPassword();
    }

    @Test
    public void whenRegisterValidCourier_thenSuccess() {
        Response registrationResponse = ApiCourier.registerCourier(validCourier);

        registrationResponse.then()
                .assertThat()
                .statusCode(201)
                .body("ok", is(true));
    }

    @Test
    public void whenRegisterDuplicateCourier_thenConflict() {
        ApiCourier.registerCourier(validCourier);

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
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
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

        testLogin = namelessCourier.getLogin();
        testPassword = namelessCourier.getPassword();
    }

    @After
    public void cleanup() {
        try {
            registeredCourierId = fetchCourierId(testLogin, testPassword);
            if (registeredCourierId != null) {
                removeTestCourier(registeredCourierId);
            }
        } catch (Exception e) {
            System.out.println("Ошибка при очистке тестовых данных: " + e.getMessage());
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