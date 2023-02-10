package couriers;

import io.restassured.response.ValidatableResponse;
//import couriers.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CourierTest {

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
    public void courierCanBeCreatedTest() {
        ValidatableResponse createResponse = courierClient.create(randomCourier);
        courierId = courierClient.login(CourierCredentials.from(randomCourier)).extract().path("id");
        check.createdSuccessfully(createResponse);
    }

    @Test
    public void identicalCourierCanNotBeCreatedTest() {
        courierClient.create(randomCourier);
        ValidatableResponse createResponse = courierClient.create(randomCourier);
        courierId = courierClient.login(CourierCredentials.from(randomCourier)).extract().path("id");
        check.creationConflicted(createResponse);
    }

    @Test
    public void courierWithoutLoginCanNotBeCreatedTest() {
        randomCourier.setLogin(null);
        ValidatableResponse createResponse = courierClient.create(randomCourier);
        check.creationFailed(createResponse);
    }

    @Test
    public void courierWithoutPasswordCanNotBeCreatedTest() {
        randomCourier.setPassword(null);
        ValidatableResponse createResponse = courierClient.create(randomCourier);
        check.creationFailed(createResponse);
    }

    @Test
    public void courierWithBusyLoginCanNotBeCreatedTest() {
        randomCourier.setLogin("IdenticalLogin");
        courierClient.create(randomCourier);
        Courier secondCourier = CourierGenerator.getRandom();
        secondCourier.setLogin("IdenticalLogin");
        ValidatableResponse createResponse = courierClient.create(secondCourier);
        courierId = courierClient.login(CourierCredentials.from(randomCourier)).extract().path("id");
        check.creationConflicted(createResponse);
    }
}
