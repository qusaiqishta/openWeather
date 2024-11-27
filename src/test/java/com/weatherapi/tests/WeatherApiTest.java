package com.weatherapi.tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.ITestResult;
import org.testng.ITestListener;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

@Listeners(WeatherApiTest.TestListener.class)
public class WeatherApiTest {

    public static class TestListener implements ITestListener {
        @Override
        public void onTestStart(ITestResult result) {
            System.out.println("Starting test: " + result.getName());
        }

        @Override
        public void onTestSuccess(ITestResult result) {
            System.out.println("Test passed: " + result.getName());
        }

        @Override
        public void onTestFailure(ITestResult result) {
            System.out.println("Test failed: " + result.getName());
        }
    }

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = TestConfig.BASE_URL;
    }

    @Test
    public void testGetWeatherForLondon() {
        given()
            .queryParam("q", "London")
            .queryParam("appid", TestConfig.API_KEY)
        .when()
            .get("/weather")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("name", equalTo("London"))
            .body("sys.country", equalTo("GB"))
            .body("coord.lon", notNullValue())
            .body("coord.lat", notNullValue());
    }

    @Test
    public void testWeatherSchemaValidation() {
        given()
            .queryParam("q", "London")
            .queryParam("appid", TestConfig.API_KEY)
        .when()
            .get("/weather")
        .then()
            .assertThat()
            .body(matchesJsonSchemaInClasspath(TestConfig.WEATHER_SCHEMA_PATH));
    }

    @Test
    public void testResponseTime() {
        given()
            .queryParam("q", "London")
            .queryParam("appid", TestConfig.API_KEY)
        .when()
            .get("/weather")
        .then()
            .time(lessThan(2000L)); // Response should be less than 2 seconds
    }

    @Test
    public void testInvalidApiKey() {
        given()
            .queryParam("q", "London")
            .queryParam("appid", "invalid_key")
        .when()
            .get("/weather")
        .then()
            .statusCode(401);
    }

    @Test
    public void testInvalidCity() {
        given()
            .queryParam("q", "NonExistentCity123456")
            .queryParam("appid", TestConfig.API_KEY)
        .when()
            .get("/weather")
        .then()
            .statusCode(404);
    }

    @Test
    public void testWeatherDataValues() {
        Response response = given()
            .queryParam("q", "London")
            .queryParam("appid", TestConfig.API_KEY)
        .when()
            .get("/weather")
        .then()
            .statusCode(200)
            .extract().response();

        // Verify temperature is within reasonable range (-100 to +100 Celsius)
        float temp = response.path("main.temp");
        assert temp > 173.15 && temp < 373.15; // Kelvin range

        // Verify humidity is between 0 and 100
        int humidity = response.path("main.humidity");
        assert humidity >= 0 && humidity <= 100;

        // Verify pressure is within reasonable range
        int pressure = response.path("main.pressure");
        assert pressure > 870 && pressure < 1085; // hPa range
    }

    @Test
    public void testAllFieldsPresent() {
        given()
            .queryParam("q", "London")
            .queryParam("appid", TestConfig.API_KEY)
        .when()
            .get("/weather")
        .then()
            .body("main.feels_like", notNullValue())
            .body("main.temp_min", notNullValue())
            .body("main.temp_max", notNullValue())
            .body("main.sea_level", notNullValue())
            .body("main.grnd_level", notNullValue())
            .body("visibility", notNullValue())
            .body("clouds.all", notNullValue())
            .body("dt", notNullValue())
            .body("sys.type", notNullValue())
            .body("sys.id", notNullValue())
            .body("sys.country", notNullValue())
            .body("sys.sunrise", notNullValue())
            .body("sys.sunset", notNullValue())
            .body("timezone", notNullValue())
            .body("id", notNullValue())
            .body("name", notNullValue())
            .body("cod", notNullValue());
    }
}
