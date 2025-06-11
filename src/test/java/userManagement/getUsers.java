package userManagement;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.AssertJUnit.assertEquals;

public class getUsers {

    @Test
    public void getUserData() {
        //given - params, header, authorization, body
        //when - url, request method
        //then - response validation, http code,
        given()
                .when()
                .get("https://reqres.in/api/users?page=2")
                .then()
                .assertThat()
                .statusCode(200);
    }

    @Test
    public void validateGetResponseBody(){
        // baseURI will have the server address
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";

        given()
                .when()
                .get("/todos/1") //end point
                .then()
                .assertThat()
                .statusCode(200)
                .body(not(emptyString()))
                .body("title", equalTo("delectus aut autem"))
                .body("userId", equalTo(1));
    }

    @Test
    public void validateResponseHasItems(){
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";

        Response response = given()
                .when()
                .get("/posts")
                .then()
                .extract()
                .response();

        assertThat(response.jsonPath().getList("title"), hasItems("eum et est occaecati", "qui est esse"));
    }

    @Test
    public void validateResponseHasSize(){
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";

        Response response = given()
                .when()
                .get("/comments")
                .then()
                .extract()
                .response();

        assertThat(response.jsonPath().getList(""), hasSize(500));
    }

    @Test
    public void validateListContainsInOrder(){
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";

        Response response = given()
                .when()
                .get("/comments?postId=1")
                .then()
                .extract()
                .response();

        List<String> expectedEmails = Arrays.asList("Eliseo@gardner.biz", "Jayne_Kuhic@sydney.com", "Nikita@garfield.biz", "Lew@alysha.tv", "Hayden@althea.biz");
        assertThat(response.jsonPath().getList("email"), contains(expectedEmails.toArray(new String[0])));
    }

    @Test
    public void testGetUsersWithQueryParameters() {
        RestAssured.baseURI = "https://reqres.in";

        Response response = given()
//                .auth()
//                .basic("postman", "password")
                .when()
                .get("/api/users?page=2")
                .then()
                .extract().response();

        System.out.println(response.prettyPrint());
        response.then().body("data", hasSize(6));

        response.then().body("data[0].id", is(7));
        response.then().body("data[0].email", is("michael.lawson@reqres.in"));
        response.then().body("data[0].first_name", is("Michael"));
        response.then().body("data[0].last_name", is("Lawson"));
        response.then().body("data[0].avatar", is("https://reqres.in/img/faces/7-image.jpg"));

    }

    @Test
    public void usingQueryParams() {
        RestAssured.baseURI = "https://reqres.in/api";

        Response response = given()
                .queryParam("page", 2)
//                .auth()
//                .basic("postman", "password")
                .when()
                .get("/users");

        response.then().assertThat().statusCode(200);

    }

    @Test
    public void multipleQueryParams() {
        RestAssured.baseURI = "https://reqres.in/api";

        Response response = given()
                .header("x-api-key", "reqres-free-v1")
                .queryParam("page", 2)
                .queryParam("per_page", 3)
                .when()
                .get("/users")
                .then()
                .statusCode(200)
                .extract().response();

//        response.then().assertThat().statusCode(200);
    }

    @Test
    public void usePathParam(){

        Response response = given()
                .pathParam("raceSeason", 2017)
                .when()
                .get("http://ergast.com/api/f1/{raceSeason}/circuits.json");

        assertEquals(response.statusCode(), 200);

        //printing response body
        System.out.println(response.body().asString());
    }

    @Test
    public void formParam() {
        Response response = given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .header("x-api-key", "reqres-free-v1")
                .formParam("name", "John Doe")
                .formParam("job", "Developer")
                .when()
                .post("https://reqres.in/api/users")
                .then()
                .statusCode(201)
                .extract().response();

        response.then().body("name", equalTo("John Doe"));
        response.then().body("job", equalTo("Developer"));
    }

    @Test
    public void getUserListWithHeader() {
        given()
                .header("Content-Type", "application/json")
                .when()
                .get("https://reqres.in/api/users?page=2")
                .then()
                .statusCode(200);
    }

    @Test
    public void getUserListWithMultipleHeader() {
        given()
                .header("Content-Type", "application/json")
                .header("x-api-key", "reqres-free-v1")
                .when()
                .get("https://reqres.in/api/users?page=2")
                .then()
                .statusCode(200);
    }

    @Test
    public void headersUsingMap() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("x-api-key", "reqres-free-v1");

        given()
                .headers(headers)
                .when()
                .get("https://reqres.in/api/users?page=2")
                .then()
                .statusCode(200);
    }

    @Test
    public void fetchHeadersFromResponse() {

        Response response = given()
                .header("x-api-key", "reqres-free-v1")
                .when()
                .get("https://reqres.in/api/users?page=2");

        Headers headers = response.getHeaders();
        for(Header header : headers) {
            if(header.getName().equals("Server")) {
                assertEquals(header.getValue(), "cloudflare");
                System.out.println(header.getName() + ": " + header.getValue());
            }

        }
    }
}

