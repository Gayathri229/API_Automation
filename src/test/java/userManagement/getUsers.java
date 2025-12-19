package userManagement;

import core.StatusCode;
import io.restassured.RestAssured;
import io.restassured.http.Cookie;
import io.restassured.http.Cookies;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import utils.JsonReader;
import utils.PropertyReader;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

public class getUsers {

    @Test
    public void getUserData() {
        //given - params, header, authorization, body
        //when - url, http request method
        //then - status code, response validation

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
        // Matchers - hasItems()
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";

        //Send a GET request and store the response in a variable
        Response response = given()
                .when()
                .get("/posts")
                .then()
                .extract()
                .response();
        // here even if we stop with get() also it's fine as it returns Response. .then() method only converts it to
        // validatableResponse, so again we're extracting the response from it. So, line 62,63,64 are not necessary.


        //here we're fetching all the titles
        assertThat(response.jsonPath().getList("title"), hasItems("eum et est occaecati", "qui est esse"));
    }

    @Test
    public void validateResponseHasSize(){
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";

        //Send a GET request and store the response in a variable
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

        //Send a GET request and store the response in a variable
        Response response = given()
                .when()
                .get("/comments?postId=1")
                .then()
                .extract()
                .response();

        //Use Hamcrest to check that the response body contains specific items in a specific order
        // contains() - checks the order as well
        List<String> expectedEmails = Arrays.asList("Eliseo@gardner.biz", "Jayne_Kuhic@sydney.com", "Nikita@garfield.biz", "Lew@alysha.tv", "Hayden@althea.biz");
        assertThat(response.jsonPath().getList("email"), contains(expectedEmails.toArray(new String[0])));
    }

    @Test
    public void testGetUsersWithQueryParameters() {
        //validate all fields of 1st record in the response body
        RestAssured.baseURI = "https://reqres.in";

        Response response = given()
                .headers("x-api-key", "reqres-free-v1")
                .when()
                .get("/api/users?page=2");
//                .then()
//                .extract().response();

        System.out.println(response.prettyPrint());
        response.then().body("data", hasSize(6));

        //Matchers - is() & equalTo() - both are same
        response.then().body("data[0].id", is(7));
        response.then().body("data[0].email", equalTo("michael.lawson@reqres.in"));
        response.then().body("data[0].first_name", is("Michael"));
        response.then().body("data[0].last_name", is("Lawson"));
        response.then().body("data[0].avatar", is("https://reqres.in/img/faces/7-image.jpg"));

    }

    @Test
    public void usingQueryParams() {
        RestAssured.baseURI = "https://reqres.in/api";

        Response response = given()
                .queryParam("page", 2)
                .when()
                .get("/users");

        int actualStatusCode = response.statusCode();
        assertEquals(actualStatusCode, 200); //testNG
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
                .pathParam("raceSeason", "2017")
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
    public void multipleHeadersUsingMap() {
        // Map to hold headers
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
        // Line - 239 --> Note the header variable is of type Header and we're traversing thru Headers
        // (Header - single header, Headers - all Headers/multiple Headers)
        for(Header header : headers) {
            if(header.getName().equals("Server")) {
                //practical example - sometimes we need to validate the server header
                assertEquals(header.getValue(), "cloudflare");
                System.out.println(header.getName() + ": " + header.getValue());
            }
        }
    }

    @Test
    public void testUseCookies() {

        given()
                .cookie("CookieKey", "CookieValue")
                .cookie("test2", "testing2")
                .when()
                .get("https://reqres.in/api/users?page=2")
                .then()
                .statusCode(200);
    }

    @Test
    public void useCookieBuilder(){
        Cookie cookie = new Cookie.Builder("CookieKey", "CookieValue")
                .setComment("Using cookies").build(); //can send multiple cookies as well

        given()
                .header("x-api-key", "reqres-free-v1")
                .cookie(cookie)
                .when()
                .get("https://reqres.in/api/users?page=2")
                .then()
                .statusCode(200);
    }

    //hypothetical scenario for the api used - doesn't pass
    @Test
    public void testFetchCookiesFromResponse(){
        Response response = given()
                .when()
                .get("https://reqres.in/api/users?page=2");

        //Like Headers class, we don't have a class for Cookies, so we directly save it into a map
        Map<String, String> cookie = response.getCookies(); //getCookies() returns a map
        assertThat(cookie, hasKey("JSESSIONID"));
        assertThat(cookie, hasValue("ABCDEF123456"));

        // Or if we use getDetailedCookies(), we can use  Cookies Class to store it
        Cookies cookies = response.getDetailedCookies(); // getDetailedCookies returns value of class Cookies type
        assertEquals(cookies.getValue("Server"), "cloudflare");
    }

    //try using map for cookies to pass it in request, and try using for loop for cookie traversal


    @Test
    public void testBasicAuthentication() {
        Response response = given()
                .auth()
                .basic("postman", "password")
                .when()
                .get("https://postman-echo.com/basic-auth");

        int actualStatusCode = response.statusCode();
        assertEquals(actualStatusCode, StatusCode.SUCCESS.code);
        System.out.println(response.body().asString());
    }


    @Test
    public void validateDeleteUser() {
        Response response = given()
                .header("x-api-key", "reqres-free-v1")
                .when()
                .delete("https://reqres.in/api/users/2");

//        response.then().statusCode(204);
        int actualStatusCode = response.statusCode();
        assertEquals(actualStatusCode, StatusCode.NO_CONTENT.code);
    }

    @Test
    public void validateWithTestDataFromJson() throws IOException, ParseException {
        String username = JsonReader.getTestData("username");
        String password = JsonReader.getTestData("password");

        Response response = given()
                .auth()
                .basic(username, password)
                .when()
                .get("https://postman-echo.com/basic-auth");

        int actualStatusCode = response.statusCode();
        assertEquals(actualStatusCode, StatusCode.SUCCESS.code);
        System.out.println(response.body().asString());
    }

    @Test
    public void validateWithDataFromPropertiesFile() {
        String userEndPoint = PropertyReader.propertyReader("config.properties", "userEndPoint");
        System.out.println("Server Address " + userEndPoint);

        Response response = given()
                .queryParam("page", 2)
                .when()
                .get(userEndPoint);

        assertEquals(response.statusCode(), 200);
    }

    @Test
    public void validateWithDataFromTestDataAndPropertiesFile() throws IOException, ParseException {
        String serverAddress = PropertyReader.propertyReader("config.properties", "serverUrl");
        String usersEndPoint = JsonReader.getTestData("usersEndPoint");

        System.out.println("Complete End point " + serverAddress + usersEndPoint);

        Response response = given()
                .queryParam("page", 2)
                .when()
                .get(serverAddress + usersEndPoint);

        assertEquals(response.statusCode(), 200);
    }

    @Test
    public void hardAssertion() {
        System.out.println("Hard Assert");
        Assert.assertTrue(false);
    }

    @Test
    public void softAssertion() {
        SoftAssert softAssert = new SoftAssert();
        System.out.println("Soft Assert");
        softAssert.assertTrue(false);
        softAssert.assertTrue(true);
        softAssert.assertAll();
    }


}

