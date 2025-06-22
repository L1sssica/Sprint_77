import api.list.ApiCourier;
import api.list.ApiLogin;
import io.restassured.response.Response;
import org.example.CreatingCourier;
import org.example.LoginCourier;
import org.example.TestDataFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.hamcrest.Matchers.*;

public class LoginCourierTest {
    private String courierId;
    private static String uniqueLogin;

    @Before
    public void setUp() {
        // Генерируем уникальный логин для каждого запуска тестов
        String uniqueLogin = "test_" + UUID.randomUUID().toString().substring(0, 8);

        // Создаем тестового курьера с уникальным логином
        CreatingCourier courier = new CreatingCourier(
                uniqueLogin,
                TestDataFactory.STANDARD_PASS,
                TestDataFactory.STANDARD_DELIVERY_PERSON_NAME
        );

        Response response = ApiCourier.registerCourier(courier);

        // Проверяем успешное создание (201) или что курьер уже существует (409)
        response.then().assertThat().statusCode(anyOf(is(201), is(409)));
    }


    @After
    public void tearDown() {
        if (courierId != null) {
            Response deleteResponse = ApiCourier.deleteCourier(courierId);
            deleteResponse.then().assertThat().statusCode(200);
        }
    }

    @Test
    public void testSuccessfulLoginWithValidCredentials() {
        LoginCourier loginData = new LoginCourier(uniqueLogin, TestDataFactory.STANDARD_PASS);
        Response response = ApiLogin.getLogin(loginData);

        response.then()
                .assertThat()
                .statusCode(200)
                .body("id", notNullValue());

        courierId = response.then().extract().path("id").toString();
    }

    @Test
    public void testLoginWithoutLoginFieldReturnsError() {
        LoginCourier loginWithoutLogin = new LoginCourier(null, TestDataFactory.STANDARD_PASS);
        Response response = ApiLogin.getLogin(loginWithoutLogin);

        response.then()
                .assertThat()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    public void testLoginWithoutPasswordFieldReturnsError() {
        LoginCourier loginWithoutPassword = new LoginCourier(uniqueLogin, null);
        Response response = ApiLogin.getLogin(loginWithoutPassword);

        response.then()
                .assertThat()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    public void testLoginWithWrongPasswordReturnsError() {
        LoginCourier loginWithWrongPassword = new LoginCourier(uniqueLogin, "wrong_password");
        Response response = ApiLogin.getLogin(loginWithWrongPassword);

        response.then()
                .assertThat()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    public void testLoginWithNonExistingUserReturnsError() {
        LoginCourier loginNonExisting = new LoginCourier("non_existing_user", "any_password");
        Response response = ApiLogin.getLogin(loginNonExisting);

        response.then()
                .assertThat()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    public void testSuccessfulLoginReturnsValidId() {
        LoginCourier loginData = new LoginCourier(uniqueLogin, TestDataFactory.STANDARD_PASS);
        Response response = ApiLogin.getLogin(loginData);

        response.then()
                .assertThat()
                .statusCode(200)
                .body("id", notNullValue())
                .body("id", greaterThan(0));

        courierId = response.then().extract().path("id").toString();
    }
}