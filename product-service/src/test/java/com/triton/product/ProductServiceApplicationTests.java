package com.triton.product;

import io.restassured.RestAssured;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MongoDBContainer;
import org.hamcrest.Matchers;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductServiceApplicationTests {

	// a lightweight, disposable MongoDB instance inside a Docker container for testing
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0.5");

//	injects the HTTP server port that was allocated at runtime.
//	a convenient alternative for @Value("${local.server.port}").
	@LocalServerPort
	private int port;

	// the method inside JUnit's @BeforeEach annotation is executed before each test method in the class.
	@BeforeEach
	void setup() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}

	// static initializer block. It is executed when the class is loaded into memory.
	static {
		mongoDBContainer.start();
	}
	@Test
	void shouldCreateProduct() {
		String requestBody = """
				{
				    "name" : "iPhone 16",
				    "description" : "iPhone 16 pro",
				    "price": 115000
				}
			""";

		RestAssured.given()
				.contentType("application/json")
				.body(requestBody)
				.when()
				.post("/api/product")
				.then()
				.statusCode(201)
				.body("id", Matchers.notNullValue())
				.body("name", Matchers.equalTo("iPhone 16"))
				.body("description", Matchers.equalTo("iPhone 16 pro"))
				.body("price", Matchers.equalTo(115000));
	}

}
