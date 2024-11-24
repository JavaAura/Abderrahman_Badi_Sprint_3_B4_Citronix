package com.citronix.feature;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import com.citronix.dto.FarmDTO;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lombok.extern.log4j.Log4j2;


@Log4j2
public class FarmControllerIntegrationTest extends BaseDev {


	@Test
	public void getAllFarms_success() {
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
	public void searchFarms_success() {
		RestAssured
				.given()
				.accept(ContentType.JSON)
				.when()
				.get("/api/farms/search?name=Farm")
				.then()
				.statusCode(HttpStatus.OK.value())
				.body("size()", Matchers.equalTo(5));
	}

	@Test
	public void getOneFarm_success() {
		RestAssured
				.given()
				.accept(ContentType.JSON)
				.when()
				.get("/api/farms/1")
				.then()
				.statusCode(HttpStatus.OK.value())
				.body("id", Matchers.equalTo(1))
				.body("fields.size()", Matchers.equalTo(4))
				.body("fields[0].treesCount", Matchers.equalTo(3));
	}

	@Test
	public void createFarm_success() {
		RestAssured
				.given()
				.contentType(ContentType.JSON)
				.body("{\"name\": \"New Farm\",\"address\": \"123 Farm Lane\",\"surface\": 1500}")
				.when()
				.post("/api/farms")
				.then()
				.statusCode(HttpStatus.CREATED.value())
				.body("id", Matchers.equalTo(6))
				.body("name", Matchers.equalTo("New Farm"))
				.body("address", Matchers.equalTo("123 Farm Lane"))
				.body("surface", Matchers.equalTo(1500.0F))
				.extract().as(FarmDTO.class);

	}

	@Test
	public void updateFarm_success() {
		FarmDTO farmDTO = RestAssured
				.given()
				.contentType(ContentType.JSON)
				.body("{\"name\": \"New Farm\",\"address\": \"123 Farm Lane\",\"surface\": 1500}")
				.when()
				.post("/api/farms")
				.then()
				.statusCode(HttpStatus.CREATED.value())
				.extract().as(FarmDTO.class);

		RestAssured
				.given()
				.contentType(ContentType.JSON)
				.body("{\"name\": \"Updated Farm\",\"address\": \"456 Updated Lane\",\"surface\": 2000}")
				.when()
				.put("/api/farms/" + farmDTO.getId())
				.then()
				.statusCode(HttpStatus.OK.value())
				.body("id", Matchers.equalTo(farmDTO.getId().intValue()))
				.body("name", Matchers.equalTo("Updated Farm"))
				.body("address", Matchers.equalTo("456 Updated Lane"))
				.body("surface", Matchers.equalTo(2000.0F));
	}

	@Test
	public void deleteFarm_success() {
		FarmDTO farmDTO = RestAssured
				.given()
				.contentType(ContentType.JSON)
				.body("{\"name\": \"New Farm\",\"address\": \"123 Farm Lane\",\"surface\": 1500}")
				.when()
				.post("/api/farms")
				.then()
				.statusCode(HttpStatus.CREATED.value())
				.extract().as(FarmDTO.class);

		RestAssured
				.given()
				.when()
				.delete("/api/farms/" + farmDTO.getId())
				.then()
				.statusCode(HttpStatus.NO_CONTENT.value());
	}

	@Test
	public void getFarm_notFound() {
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
	public void createFarm_invalidInput() {
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
