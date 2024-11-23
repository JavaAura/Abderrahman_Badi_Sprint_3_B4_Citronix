package com.citronix.feature;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class HarvestDetailsControllerIntegrationTest extends BaseDev {

	@Test
	void getAllHarvests_success() {
		RestAssured
				.given()
				.accept(ContentType.JSON)
				.when()
				.get("/api/harvest-details")
				.then()
				.statusCode(HttpStatus.OK.value())
				.body("size()", Matchers.equalTo(12));
	}

	@Test
	void getOneHarvest_success() {
		RestAssured
				.given()
				.accept(ContentType.JSON)
				.when()
				.get("/api/harvest-details/harvests/1/trees/1")
				.then()
				.statusCode(HttpStatus.OK.value())
				.body("harvest.id", Matchers.equalTo(1))
				.body("tree.id", Matchers.equalTo(1))
				.body("totalYield", Matchers.equalTo(200.0F));
	}

	@Test
	void createHarvestDetails_success() {
		RestAssured
				.given()
				.contentType(ContentType.JSON)
				.body("{\"harvestId\": 1,\"treeId\": 14,\"yield\": 100.0,\"harvestedAt\": \"19-08-2024\"}")
				.when()
				.post("/api/harvest-details")
				.then()
				.statusCode(HttpStatus.CREATED.value())
				.body("harvest.id", Matchers.equalTo(1))
				.body("tree.id", Matchers.equalTo(14))
				.body("yield", Matchers.equalTo(100.0F));
	}

	@Test
	void updateHarvest_success() {
		RestAssured
				.given()
				.contentType(ContentType.JSON)
				.body("{\"harvestId\": 1,\"treeId\": 14,\"yield\": 100.0,\"harvestedAt\": \"19-08-2024\"}")
				.when()
				.post("/api/harvest-details")
				.then()
				.statusCode(HttpStatus.CREATED.value());

		RestAssured
				.given()
				.contentType(ContentType.JSON)
				.body("{\"harvestId\": 1,\"treeId\": 14,\"yield\": 300.0,\"harvestedAt\": \"19-08-2024\"}")
				.when()
				.put("/api/harvest-details/harvests/1/trees/14")
				.then()
				.statusCode(HttpStatus.CREATED.value())
				.body("harvest.id", Matchers.equalTo(1))
				.body("tree.id", Matchers.equalTo(14))
				.body("yield", Matchers.equalTo(500.0F));
	}

	@Test
	void deleteHarvest_success() {
		RestAssured
				.given()
				.contentType(ContentType.JSON)
				.body("{\"harvestId\": 1,\"treeId\": 14,\"yield\": 100.0,\"harvestedAt\": \"19-08-2024\"}")
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
	void getHarvest_notFound() {
		RestAssured
				.given()
				.accept(ContentType.JSON)
				.when()
				.get("/api/harvest-details/harvests/999/trees/999")
				.then()
				.statusCode(HttpStatus.NOT_FOUND.value())
				.body("message", Matchers.equalTo("Harvest not found"))
				.body("status", Matchers.equalTo(404));
	}
}
