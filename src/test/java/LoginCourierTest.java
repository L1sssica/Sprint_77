import ApiList.ApiCourier;
import ApiList.ApiLogin;
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
    private static boolean courierCreated = false;

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
        // Удаляем тестового курьера после всех тестов, если он был создан
        if (courierId != null) {
            Response deleteResponse = ApiCourier.deleteCourier(courierId);
            deleteResponse.then().assertThat().statusCode(200);
        }
    }

    @Test
    public void testCourierCanLoginWithValidCredentials() {
        // Проверяем, что курьер может авторизоваться с валидными данными
        LoginCourier loginData = TestDataFactory.getStandardAuthCredentials();
        Response response = ApiLogin.getLogin(loginData);

        response.then()
                .assertThat()
                .statusCode(200)
                .body("id", notNullValue());

        // Сохраняем ID для последующего удаления
        courierId = response.then().extract().path("id").toString();
    }

    @Test
    public void testLoginRequiresAllMandatoryFields() {
        // Проверяем, что для авторизации нужны все обязательные поля

        // Без логина
        Response responseWithoutLogin = ApiCourier.authenticateCourier("", TestDataFactory.STANDARD_PASS);
        responseWithoutLogin.then()
                .assertThat()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));

        // Без пароля
        Response responseWithoutPassword = ApiCourier.authenticateCourier(TestDataFactory.STANDARD_USERNAME, "");
        responseWithoutPassword.then()
                .assertThat()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    public void testLoginWithIncorrectCredentialsReturnsError() {
        // Проверяем, что система вернет ошибку при неверных учетных данных

        // Неправильный пароль
        Response responseWrongPassword = ApiCourier.authenticateCourier(
                TestDataFactory.STANDARD_USERNAME,
                "wrong_password"
        );
        responseWrongPassword.then()
                .assertThat()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));

        // Неправильный логин
        Response responseWrongLogin = ApiCourier.authenticateCourier(
                "non_existing_user",
                TestDataFactory.STANDARD_PASS
        );
        responseWrongLogin.then()
                .assertThat()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    public void testLoginWithMissingFieldReturnsError() {
        // Проверяем, что запрос без обязательного поля возвращает ошибку

        // Создаем объект с отсутствующим полем пароля
        LoginCourier loginWithoutPassword = new LoginCourier(TestDataFactory.STANDARD_USERNAME, null);
        Response responseMissingPassword = ApiLogin.getLogin(loginWithoutPassword);
        responseMissingPassword.then()
                .assertThat()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));

        // Создаем объект с отсутствующим полем логина
        LoginCourier loginWithoutUsername = new LoginCourier(null, TestDataFactory.STANDARD_PASS);
        Response responseMissingUsername = ApiLogin.getLogin(loginWithoutUsername);
        responseMissingUsername.then()
                .assertThat()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    public void testLoginWithNonExistingUserReturnsError() {
        // Проверяем, что авторизация под несуществующим пользователем возвращает ошибку
        Response response = ApiCourier.authenticateCourier(
                "non_existing_user_123",
                "random_password_123"
        );
        response.then()
                .assertThat()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    public void testSuccessfulLoginReturnsId() {
        // Проверяем, что успешный запрос возвращает ID
        LoginCourier loginData = TestDataFactory.getStandardAuthCredentials();
        Response response = ApiLogin.getLogin(loginData);

        response.then()
                .assertThat()
                .statusCode(200)
                .body("id", notNullValue())
                .body("id", greaterThan(0));

        // Сохраняем ID для последующего удаления
        courierId = response.then().extract().path("id").toString();
    } // Не понимаю, почему некоторые тесты завершаются ошибкой 504. Укажите, пожалуйста, на ошибки.
}