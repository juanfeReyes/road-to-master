package com.r2m.package_manager_api.integration

import org.junit.jupiter.api.TestInstance
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.postgresql.PostgreSQLContainer

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
abstract class IntegrationTestBase {

    companion object {

        @Container
        @ServiceConnection
        @JvmStatic
        val postgresDb = PostgreSQLContainer("postgres:9.6.12");

    }
}