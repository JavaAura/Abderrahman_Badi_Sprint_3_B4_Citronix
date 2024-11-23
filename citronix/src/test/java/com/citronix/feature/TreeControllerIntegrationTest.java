package com.citronix.feature;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class TreeControllerIntegrationTest extends BaseDev {

	@Test
	void getAllTrees_success() {
		RestAssured
				.given()
				.accept(ContentType.JSON)
				.when()
				.get("/api/trees")
				.then()
				.statusCode(HttpStatus.OK.value())
				.body("size()", Matchers.equalTo(17));
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
				.body("id", Matchers.equalTo(1))
				.body("age", Matchers.equalTo(5));
	}

	@Test
	void createTree_success() {
		RestAssured
				.given()
				.contentType(ContentType.JSON)
				.body("{\"plantedAt\": \"03-01-2021\",\"field\": {\"id\": \"1\"}}")
				.when()
				.post("/api/trees")
				.then()
				.statusCode(HttpStatus.CREATED.value())
				.body("id", Matchers.equalTo(18))
				.body("age", Matchers.equalTo(3))
				.body("annualProductivity", Matchers.equalTo(12.0F));
	}

	@Test
	void updateTree_success() {
		RestAssured
				.given()
				.contentType(ContentType.JSON)
				.body("{\"plantedAt\": \"03-01-2021\",\"field\": {\"id\": \"1\"}}")
				.when()
				.post("/api/trees")
				.then()
				.statusCode(HttpStatus.CREATED.value());

		RestAssured
				.given()
				.contentType(ContentType.JSON)
				.body("{\"plantedAt\": \"03-01-2021\",\"field\": {\"id\": \"2\"}}")
				.when()
				.put("/api/trees/18")
				.then()
				.statusCode(HttpStatus.OK.value())
				.body("id", Matchers.equalTo(18))
				.body("age", Matchers.equalTo(3))
				.body("annualProductivity", Matchers.equalTo(12.0F));
	}

	@Test
	void deleteTree_success() {
		RestAssured
				.given()
				.contentType(ContentType.JSON)
				.body("{\"plantedAt\": \"03-01-2021\",\"field\": {\"id\": \"1\"}}")
				.when()
				.post("/api/trees")
				.then()
				.statusCode(HttpStatus.CREATED.value());

		RestAssured
				.given()
				.when()
				.delete("/api/trees/18")
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
				.body("{\"plantedAt\": \"33-01-2021\",\"field\": {\"id\": \"1\"}}")
				.when()
				.post("/api/trees")
				.then()
				.statusCode(HttpStatus.BAD_REQUEST.value())
				.body("message", Matchers.notNullValue())
				.body("status", Matchers.equalTo(400));
	}

	@Test
	void createTree_insufficientSurface() {
		RestAssured
				.given()
				.contentType(ContentType.JSON)
				.body("{\"plantedAt\": \"03-01-2021\",\"field\": {\"id\": \"1\"}}")
				.when()
				.post("/api/trees")
				.then()
				.statusCode(HttpStatus.CREATED.value());

		RestAssured
				.given()
				.contentType(ContentType.JSON)
				.body("{\"plantedAt\": \"03-01-2021\",\"field\": {\"id\": \"1\"}}")
				.when()
				.post("/api/trees")
				.then()
				.statusCode(HttpStatus.CREATED.value());

		RestAssured
				.given()
				.contentType(ContentType.JSON)
				.body("{\"plantedAt\": \"03-01-2021\",\"field\": {\"id\": \"1\"}}")
				.when()
				.post("/api/trees")
				.then()
				.statusCode(HttpStatus.BAD_REQUEST.value())
				.body("message", Matchers.equalTo("Insuffissant surface in the selected field"))
				.body("status", Matchers.equalTo(400));
	}

}
