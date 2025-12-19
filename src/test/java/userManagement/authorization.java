package userManagement;

import io.restassured.response.Response;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;


public class authorization {

    @Test
    public void validateBasicAuthentication() {
        Response response = given()
                .auth()
                .basic("postman", "password")
                .when()
                .get("https://postman-echo.com/basic-auth");

        int actualStatusCode = response.statusCode();
        assertEquals(actualStatusCode, 200);
        System.out.println(response.body().asString());
    }

    @Test
    public void validateDigestAuthentication(){
        Response response = given()
                .auth()
                .digest("postman", "password")
                .when()
                .get("https://postman-echo.com/digest-auth");

        int actualStatusCode = response.statusCode();
        assertEquals(actualStatusCode, 200);
        System.out.println(response.body().asString());
    }
}
