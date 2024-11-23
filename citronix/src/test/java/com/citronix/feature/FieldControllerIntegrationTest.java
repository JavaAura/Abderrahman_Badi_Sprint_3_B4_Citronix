package com.citronix.feature;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class FieldControllerIntegrationTest extends BaseDev {

	@Test
	void getAllFields_success() {
		RestAssured
				.given()
				.accept(ContentType.JSON)
				.when()
				.get("/api/fields")
				.then()
				.statusCode(HttpStatus.OK.value())
				.body("size()", Matchers.equalTo(16));
	}

	@Test
	void getOneField_success() {
		RestAssured
				.given()
				.accept(ContentType.JSON)
				.when()
				.get("/api/fields/1")
				.then()
				.statusCode(HttpStatus.OK.value())
				.body("id", Matchers.equalTo(1))
				.body("trees.size()", Matchers.equalTo(3));
	}

	@Test
	void createField_success() {
		RestAssured
				.given()
				.contentType(ContentType.JSON)
				.body("{\"surface\": 1000,\"farm\":{\"id\": 5}}")
				.when()
				.post("/api/fields")
				.then()
				.statusCode(HttpStatus.CREATED.value())
				.body("surface", Matchers.equalTo(1000.0F))
				.body("id", Matchers.equalTo(17));
	}

	@Test
	void updateField_success() {
		RestAssured
				.given()
				.contentType(ContentType.JSON)
				.body("{\"surface\": 1000,\"farm\":{\"id\": 5}}")
				.when()
				.post("/api/fields")
				.then()
				.statusCode(HttpStatus.CREATED.value());

		RestAssured
				.given()
				.contentType(ContentType.JSON)
				.body("{\"surface\": 2000,\"farm\":{\"id\": 5}}")
				.when()
				.put("/api/fields/17")
				.then()
				.statusCode(HttpStatus.OK.value())
				.body("surface", Matchers.equalTo(2000.0F))
				.body("id", Matchers.equalTo(17));
	}

	@Test
	void deleteField_success() {
		RestAssured
				.given()
				.contentType(ContentType.JSON)
				.body("{\"surface\": 1000,\"farm\":{\"id\": 5}}")
				.when()
				.post("/api/fields")
				.then()
				.statusCode(HttpStatus.CREATED.value());

		RestAssured
				.given()
				.when()
				.delete("/api/fields/17")
				.then()
				.statusCode(HttpStatus.NO_CONTENT.value());
	}

	@Test
	void getField_notFound() {
		RestAssured
				.given()
				.accept(ContentType.JSON)
				.when()
				.get("/api/fields/999")
				.then()
				.statusCode(HttpStatus.NOT_FOUND.value())
				.body("message", Matchers.equalTo("Field not found"))
				.body("status", Matchers.equalTo(404));
	}

	@Test
	void createField_invalidInput() {
		RestAssured
				.given()
				.contentType(ContentType.JSON)
				.body("{\"surface\": -100,\"farm\":{\"id\": 5}}")
				.when()
				.post("/api/fields")
				.then()
				.statusCode(HttpStatus.BAD_REQUEST.value())
				.body("message", Matchers.notNullValue())
				.body("status", Matchers.equalTo(400));
	}

	@Test
	void createField_throwsInvalidSurface() {
		RestAssured
				.given()
				.contentType(ContentType.JSON)
				.body("{\"surface\": 3000,\"farm\":{\"id\": 5}}")
				.when()
				.post("/api/fields")
				.then()
				.statusCode(HttpStatus.BAD_REQUEST.value())
				.body("message", Matchers.equalTo("Surface provided surpasses farm capacity"))
				.body("status", Matchers.equalTo(400));
	}

}
