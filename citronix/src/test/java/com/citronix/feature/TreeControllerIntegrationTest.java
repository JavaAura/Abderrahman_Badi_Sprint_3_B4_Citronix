package com.citronix.feature;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@Transactional
@Rollback(true)
public class TreeControllerIntegrationTest {

    @Test
    void getAllTrees_success() {
        RestAssured
                .given()
                .accept(ContentType.JSON)
                .when()
                .get("/api/trees")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", Matchers.equalTo(20));
    }

    @Test
    void getOneTree_success() {
        RestAssured
                .given()
                .accept(ContentType.JSON)
                .when()
                .get("/api/trees/1")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", Matchers.equalTo(1))
                .body("id", Matchers.equalTo(1));
    }

    @Test
    void createTree_success() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body("{\"client\": \"John Doe\",\"saleDate\": \"22-03-2024\",\"unitPrice\": 1000,\"harvest\":{\"id\" : 5}}")
                .when()
                .post("/api/trees")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("client", Matchers.equalTo("John Doe"))
                .body("unitPrice", Matchers.equalTo(1000.0))
                .body("totalRevenue", Matchers.equalTo(10000.0));
    }

    @Test
    void updateTree_success() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body("{\"client\": \"John Doe\",\"saleDate\": \"22-03-2024\",\"unitPrice\": 1000,\"harvest\":{\"id\" : 5}}")
                .when()
                .post("/api/trees")
                .then()
                .statusCode(HttpStatus.CREATED.value());

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body("{\"client\": \"\",\"saleDate\": null,\"unitPrice\": null,\"harvest\":{\"id\" : 6}}")
                .when()
                .put("/api/trees/21")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("client", Matchers.equalTo("John Doe"))
                .body("unitPrice", Matchers.equalTo(1000.0))
                .body("totalRevenue", Matchers.equalTo(10000.0));
    }

    @Test
    void deleteTree_success() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body("{\"client\": \"John Doe\",\"saleDate\": \"22-03-2024\",\"unitPrice\": 1000,\"harvest\":{\"id\" : 5}}")
                .when()
                .post("/api/trees")
                .then()
                .statusCode(HttpStatus.CREATED.value());

        RestAssured
                .given()
                .when()
                .delete("/api/trees/21")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void getTree_notFound() {
        RestAssured
                .given()
                .accept(ContentType.JSON)
                .when()
                .get("/api/trees/999")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("message", Matchers.equalTo("Tree not found"))
                .body("status", Matchers.equalTo(404));
    }

    @Test
    void createTree_invalidInput() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body("{\"client\": \"John Doe\",\"saleDate\": \"22-03-2024\",\"unitPrice\": -1000,\"harvest\":{\"id\" : 5}}")
                .when()
                .post("/api/trees")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", Matchers.notNullValue())
                .body("status", Matchers.equalTo(400));
    }

}
