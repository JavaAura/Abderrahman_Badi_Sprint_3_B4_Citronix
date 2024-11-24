package com.citronix.feature;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class HarvestDetailsControllerIntegrationTest extends BaseDev {

	@Test
	void getOneHarvestDetails_success() {
		RestAssured
				.given()
				.accept(ContentType.JSON)
				.when()
				.get("/api/harvest-details/harvests/1/trees/1")
				.then()
				.statusCode(HttpStatus.OK.value())
				.body("harvest.id", Matchers.equalTo(1))
				.body("tree.id", Matchers.equalTo(1))
				.body("yield", Matchers.equalTo(200.0F))
				.body("harvestedAt", Matchers.equalTo("2024-11-20"));
	}

	@Test
	void createHarvestDetail_success() {
		RestAssured
				.given()
				.contentType(ContentType.JSON)
				.body("{\"harvest\": {\"id\": 1},\"tree\": {\"id\": 14},\"yield\": 100,\"harvestedAt\": \"19-08-2024\"}")
				.when()
				.post("/api/harvest-details")
				.then()
				.statusCode(HttpStatus.CREATED.value())
				.body("harvest.id", Matchers.equalTo(1))
				.body("tree.id", Matchers.equalTo(14))
				.body("yield", Matchers.equalTo(100.0F))
				.body("harvestedAt", Matchers.equalTo("2024-08-19"));
	}

	@Test
	void updateHarvestDetail_success() {
		RestAssured
				.given()
				.contentType(ContentType.JSON)
				.body("{\"harvest\": {\"id\": 1},\"tree\": {\"id\": 14},\"yield\": 100,\"harvestedAt\": \"19-08-2024\"}")
				.when()
				.post("/api/harvest-details")
				.then()
				.statusCode(HttpStatus.CREATED.value());

		RestAssured
				.given()
				.contentType(ContentType.JSON)
				.body("{\"harvest\": {\"id\": 1},\"tree\": {\"id\": 14},\"yield\": 500,\"harvestedAt\": \"19-08-2024\"}")
				.when()
				.put("/api/harvest-details/harvests/1/trees/14")
				.then()
				.statusCode(HttpStatus.OK.value())
				.body("harvest.id", Matchers.equalTo(1))
				.body("tree.id", Matchers.equalTo(14))
				.body("yield", Matchers.equalTo(500.0F));
	}

	@Test
	void deleteHarvestDetail_success() {
		RestAssured
				.given()
				.contentType(ContentType.JSON)
				.body("{\"harvest\": {\"id\": 1},\"tree\": {\"id\": 14},\"yield\": 100,\"harvestedAt\": \"19-08-2024\"}")
				.when()
				.post("/api/harvest-details")
				.then()
				.statusCode(HttpStatus.CREATED.value());

		RestAssured
				.given()
				.when()
				.delete("/api/harvest-details/harvests/1/trees/14")
				.then()
				.statusCode(HttpStatus.NO_CONTENT.value());
	}

	@Test
	void getHarvestDetail_notFound() {
		RestAssured
				.given()
				.accept(ContentType.JSON)
				.when()
				.get("/api/harvest-details/harvests/999/trees/999")
				.then()
				.statusCode(HttpStatus.NOT_FOUND.value())
				.body("message", Matchers.equalTo("Harvest detail not found"))
				.body("status", Matchers.equalTo(404));
	}

	@Test
	void getHarvestDetail_duplicateResource() {
		RestAssured
				.given()
				.contentType(ContentType.JSON)
				.body("{\"harvest\": {\"id\": 1},\"tree\": {\"id\": 1},\"yield\": 100,\"harvestedAt\": \"19-08-2024\"}")
				.when()
				.post("/api/harvest-details")
				.then()
				.statusCode(HttpStatus.BAD_REQUEST.value())
				.body("message", Matchers.equalTo("The tree is already harvested"))
                .body("status", Matchers.equalTo(400));
	}
}
