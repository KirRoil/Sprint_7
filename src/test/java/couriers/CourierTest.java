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
    public void courierCanBeCreated() {
        ValidatableResponse createResponse = courierClient.create(randomCourier);
        check.createdSuccessfully(createResponse);

        courierId = courierClient.login(CourierCredentials.from(randomCourier)).extract().path("id");
    }

    @Test
    public void identicalCourierCanNotBeCreated() {
        courierClient.create(randomCourier);
        ValidatableResponse createResponse = courierClient.create(randomCourier);
        check.creationConflicted(createResponse);

        courierId = courierClient.login(CourierCredentials.from(randomCourier)).extract().path("id");
    }

    @Test
    public void courierWithoutLoginCanNotBeCreated() {
        randomCourier.setLogin(null);
        ValidatableResponse createResponse = courierClient.create(randomCourier);
        check.creationFailed(createResponse);
    }

    @Test
    public void courierWithoutPasswordCanNotBeCreated() {
        randomCourier.setPassword(null);
        ValidatableResponse createResponse = courierClient.create(randomCourier);
        check.creationFailed(createResponse);
    }

    @Test
    public void courierWithBusyLoginCanNotBeCreated() {
        randomCourier.setLogin("IdenticalLogin");
        courierClient.create(randomCourier);
        Courier secondCourier = CourierGenerator.getRandom();
        secondCourier.setLogin("IdenticalLogin");
        ValidatableResponse createResponse = courierClient.create(secondCourier);
        check.creationConflicted(createResponse);

        courierId = courierClient.login(CourierCredentials.from(randomCourier)).extract().path("id");
    }
}
