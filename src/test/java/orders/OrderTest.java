package orders;

import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class OrderTest {
    private Orders defaultOrder;
    private OrdersClient ordersClient;
    private OrdersAssertions check;
    private int orderId;

    @Before
    public void setUp() {
        ordersClient = new OrdersClient();
        check = new OrdersAssertions();
        defaultOrder = OrdersGenerator.getDefault();
    }

    @After
    public void cleanUp() {
        ordersClient.cancel(orderId);
    }

    @Test
    public void listOfOrdersCanBeGot() {
        ValidatableResponse createResponse = ordersClient.create(defaultOrder);
        ValidatableResponse getResponse = ordersClient.returnListOfOrders();
        check.gettingSuccessfully(getResponse);
        orderId = createResponse.extract().path("track");
    }
}
