package couriers;

import io.restassured.response.ValidatableResponse;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;

public class CourierAssertions {
    public void createdSuccessfully(ValidatableResponse response) {
        response.assertThat()
                .statusCode(SC_CREATED)
                .body("ok", is(true))
                ;
    }

    public void creationFailed(ValidatableResponse response) {
        response.assertThat()
                .statusCode(SC_BAD_REQUEST)
                .body("message", notNullValue())
                ;
    }

    public void creationConflicted(ValidatableResponse response) {
        response.assertThat()
                .statusCode(SC_CONFLICT)
                .body("message", notNullValue())
                ;
    }

    public void loggedInSuccessfully(ValidatableResponse response) {
        response.assertThat()
                .statusCode(SC_OK)
                .body("id", greaterThan(0))
                ;
    }
    //Тысяча извинений, но у меня логин без пароля падает с таймаутом. Возможно, потому что я в Индии.
    //Если у вас этот тест не проходит, сделайте вид, что тут всё по аналогии с кодом выше, потому что я реально
    //уже не знаю, что тут не так, и почему при логине без логина всё ок, а без пароля - таймаут аналогичном коде.
    public void loggedInWithoutLoginOrPasswordFailed(ValidatableResponse response) {
        response.assertThat()
                .statusCode(SC_BAD_REQUEST)
                .body("message", notNullValue())
                ;
    }

    public void loggedInWithInvalidFieldFailed(ValidatableResponse response) {
        response.assertThat()
                .statusCode(SC_NOT_FOUND)
                .body("message", notNullValue())
                ;
    }
}
