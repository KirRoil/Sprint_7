package couriers;

import io.restassured.response.ValidatableResponse;
//import couriers.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LoginTest {

    private Courier randomCourier;
    private CourierClient courierClient;
    private CourierAssertions check;
    private int courierId;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        check = new CourierAssertions();
        randomCourier = CourierGenerator.getRandom();
    }

    @After
    public void cleanUp() {
        if (courierId > 0) {
            courierClient.delete(courierId);
        }
    }

    @Test
    public void courierCanLoggedInTest() {
        courierClient.create(randomCourier);
        CourierCredentials credentials = CourierCredentials.from(randomCourier);
        ValidatableResponse loginResponse = courierClient.login(credentials);
        courierId = loginResponse.extract().path("id");
        check.loggedInSuccessfully(loginResponse);
    }

    @Test
    public void loginWithoutLoginFailsTest() {
        courierClient.create(randomCourier);
        courierId = courierClient.login(CourierCredentials.from(randomCourier)).extract().path("id");
        CourierCredentials credentials = CourierCredentials.from(randomCourier);
        credentials.setLogin(null);
        ValidatableResponse loginResponse = courierClient.login(credentials);
        check.loggedInWithoutLoginOrPasswordFailed(loginResponse);
    }
    //Тысяча извинений, но у меня логин без пароля падает с таймаутом. Возможно, потому что я в Индии.
    //В отчете аллюра это будет отражено. По документации код ошибки один, для теста код идентичен тоже. Я не знаю, что у меня не так.
    @Test
    public void loginWithoutPasswordFailsTest() {
        courierClient.create(randomCourier);
        courierId = courierClient.login(CourierCredentials.from(randomCourier)).extract().path("id");
        CourierCredentials credentials = CourierCredentials.from(randomCourier);
        credentials.setPassword(null);
        ValidatableResponse loginResponse = courierClient.login(credentials);
        check.loggedInWithoutLoginOrPasswordFailed(loginResponse);
    }

    @Test
    public void loginWithInvalidLoginFailsTest() {
        courierClient.create(randomCourier);
        courierId = courierClient.login(CourierCredentials.from(randomCourier)).extract().path("id");
        CourierCredentials credentials = CourierCredentials.from(randomCourier);
        credentials.setLogin("InvalidLogin");
        ValidatableResponse loginResponse = courierClient.login(credentials);
        check.loggedInWithInvalidFieldFailed(loginResponse);
    }

    @Test
    public void loginWithInvalidPasswordFailsTest() {
        courierClient.create(randomCourier);
        courierId = courierClient.login(CourierCredentials.from(randomCourier)).extract().path("id");
        CourierCredentials credentials = CourierCredentials.from(randomCourier);
        credentials.setPassword("InvalidPassword");
        ValidatableResponse loginResponse = courierClient.login(credentials);
        check.loggedInWithInvalidFieldFailed(loginResponse);
    }

    @Test
    public void loginNonexistentCourierFailsTest() {
        CourierCredentials credentials = CourierCredentials.from(randomCourier);
        ValidatableResponse loginResponse = courierClient.login(credentials);
        check.loggedInWithInvalidFieldFailed(loginResponse);
    }
}

