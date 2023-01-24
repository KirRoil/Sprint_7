package orders;

import io.restassured.response.ValidatableResponse;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;

public class OrdersAssertions {
    public void creationSuccessfully(ValidatableResponse response) {
        response.assertThat()
                .statusCode(SC_CREATED)
                .body("track", greaterThan(0))
        ;
    }

    public void gettingSuccessfully(ValidatableResponse response) {
        response.assertThat()
                .statusCode(SC_OK)
                .body("orders", notNullValue())
        ;
    }
}
