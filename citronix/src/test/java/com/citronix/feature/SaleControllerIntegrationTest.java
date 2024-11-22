package com.citronix.feature;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class SaleControllerIntegrationTest extends BaseDev {

    @Test
    void getAllSales_success() {
        RestAssured
                .given()
                .accept(ContentType.JSON)
                .when()
                .get("/api/sales")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", Matchers.equalTo(1));
    }

    @Test
    void getOneSale_success() {
        RestAssured
                .given()
                .accept(ContentType.JSON)
                .when()
                .get("/api/sales/1")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", Matchers.equalTo(1))
                .body("id", Matchers.equalTo(1));
    }

    @Test
    void createSale_success() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body("{\"client\": \"John Doe\",\"saleDate\": \"22-03-2024\",\"unitPrice\": 1000,\"harvest\":{\"id\" : 2}}")
                .when()
                .post("/api/sales")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("client", Matchers.equalTo("John Doe"))
                .body("unitPrice", Matchers.equalTo(1000.0))
                .body("totalRevenue", Matchers.equalTo(10000.0));
    }

    @Test
    void updateSale_success() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body("{\"client\": \"John Doe\",\"saleDate\": \"22-03-2024\",\"unitPrice\": 1000,\"harvest\":{\"id\" : 2}}")
                .when()
                .post("/api/sales")
                .then()
                .statusCode(HttpStatus.CREATED.value());

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body("{\"client\": \"\",\"saleDate\": null,\"unitPrice\": null,\"harvest\":{\"id\" : 3}}")
                .when()
                .put("/api/sales/2")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("client", Matchers.equalTo("John Doe"))
                .body("unitPrice", Matchers.equalTo(1000.0))
                .body("totalRevenue", Matchers.equalTo(10000.0));
    }

    @Test
    void deleteSale_success() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body("{\"client\": \"John Doe\",\"saleDate\": \"22-03-2024\",\"unitPrice\": 1000,\"harvest\":{\"id\" : 2}}")
                .when()
                .post("/api/sales")
                .then()
                .statusCode(HttpStatus.CREATED.value());

        RestAssured
                .given()
                .when()
                .delete("/api/sales/11")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void getSale_notFound() {
        RestAssured
                .given()
                .accept(ContentType.JSON)
                .when()
                .get("/api/sales/999")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("message", Matchers.equalTo("Sale not found"))
                .body("status", Matchers.equalTo(404));
    }

    @Test
    void createSale_invalidInput() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body("{\"client\": \"John Doe\",\"saleDate\": \"22-03-2024\",\"unitPrice\": -1000,\"harvest\":{\"id\" : 5}}")
                .when()
                .post("/api/sales")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", Matchers.notNullValue())
                .body("status", Matchers.equalTo(400));
    }

}
