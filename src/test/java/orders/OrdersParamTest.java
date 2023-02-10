package orders;

import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class OrdersParamTest {
    private Orders defaultOrder;
    private OrdersClient ordersClient;
    private OrdersAssertions check;
    private int orderId;
    @Parameterized.Parameter()
    public String[] color;

    @Before
    public void setUp() {
        ordersClient = new OrdersClient();
        check = new OrdersAssertions();
        defaultOrder = OrdersGenerator.getDefault();
    }

    @After
    public void cleanUp() {
        if (orderId > 0) {
            ordersClient.cancel(orderId);
        }
    }

    @Parameterized.Parameters()
    public static Object[] params() {
        return new Object[][]{
                {new String[]{"BLACK"}},
                {new String[]{"GRAY"}},
                {new String[]{"BLACK", "GRAY"}},
                {new String[]{}},
        };
    }

    @Test
    public void orderCanBeCreatedWithColorTest() {
        defaultOrder.setColor(color);
        ValidatableResponse createResponse = ordersClient.create(defaultOrder);
        orderId = createResponse.extract().path("track");
        check.creationSuccessfully(createResponse);
    }
}
