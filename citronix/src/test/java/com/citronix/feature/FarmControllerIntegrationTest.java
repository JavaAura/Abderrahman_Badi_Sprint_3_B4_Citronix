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
public class FarmControllerIntegrationTest extends BaseDev {

    @Test
    void getAllFarms_success() {
        RestAssured
                .given()
                .accept(ContentType.JSON)
                .when()
                .get("/api/farms")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", Matchers.equalTo(5));
    }

    @Test
    void getOneFarm_success() {
        RestAssured
                .given()
                .accept(ContentType.JSON)
                .when()
                .get("/api/farms/1")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", Matchers.equalTo(1))
                .body("id", Matchers.equalTo(1));
    }

    @Test
    void createFarm_success() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body("{\"name\": \"New Farm\",\"address\": \"123 Farm Lane\",\"surface\": 1500}")
                .when()
                .post("/api/farms")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("name", Matchers.equalTo("New Farm"))
                .body("address", Matchers.equalTo("123 Farm Lane"))
                .body("surface", Matchers.equalTo(1500.0));
    }

    @Test
    void updateFarm_success() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body("{\"name\": \"New Farm\",\"address\": \"123 Farm Lane\",\"surface\": 1500}")
                .when()
                .post("/api/farms")
                .then()
                .statusCode(HttpStatus.CREATED.value());

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body("{\"name\": \"Updated Farm\",\"address\": \"456 Updated Lane\",\"surface\": 2000}")
                .when()
                .put("/api/farms/6")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", Matchers.equalTo(1))
                .body("name", Matchers.equalTo("Updated Farm"))
                .body("address", Matchers.equalTo("456 Updated Lane"))
                .body("surface", Matchers.equalTo(2000.0));
    }

    @Test
    void deleteFarm_success() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body("{\"name\": \"New Farm\",\"address\": \"123 Farm Lane\",\"surface\": 1500}")
                .when()
                .post("/api/farms")
                .then()
                .statusCode(HttpStatus.CREATED.value());

        RestAssured
                .given()
                .when()
                .delete("/api/farms/6")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void getFarm_notFound() {
        RestAssured
                .given()
                .accept(ContentType.JSON)
                .when()
                .get("/api/farms/999")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("message", Matchers.equalTo("Farm not found"))
                .body("status", Matchers.equalTo(404));
    }

    @Test
    void createFarm_invalidInput() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body("{\"name\": \"\",\"address\": \"123 Farm Lane\",\"surface\": -100}")
                .when()
                .post("/api/farms")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", Matchers.notNullValue())
                .body("status", Matchers.equalTo(400));
    }
}
