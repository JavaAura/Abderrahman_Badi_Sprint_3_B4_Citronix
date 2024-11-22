package com.citronix.feature;

import java.sql.Connection;
import java.sql.SQLException;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import com.citronix.App;

import io.restassured.RestAssured;

@SpringBootTest(classes = App.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
public abstract class BaseDev {

    @LocalServerPort
    public int serverPort;

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    public void initRestAssured() {
        RestAssured.port = serverPort;
        RestAssured.urlEncodingEnabled = false;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @AfterEach
    public void resetDatabase() {
        try (Connection connection = dataSource.getConnection()) {
            connection.createStatement().execute("DROP ALL OBJECTS;");

            try (Liquibase liquibase = new Liquibase(
                    "classpath:db/changelog/db.changelog-master.yaml",
                    new ClassLoaderResourceAccessor(),
                    new JdbcConnection(connection));) {
                liquibase.update("");
            }

        } catch (SQLException | LiquibaseException e) {
            throw new RuntimeException("Error setting up database", e);
        }
    }
}
