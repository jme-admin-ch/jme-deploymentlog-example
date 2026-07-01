package ch.admin.bit.jme.deploymentlog;

import ch.admin.bit.jeap.jme.test.BootServiceSpringIntegrationTestBase;
import io.restassured.http.ContentType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@EnableWebSecurity
class DeploymentLogExampleIT extends BootServiceSpringIntegrationTestBase {

    private static final String SERVICE_BASE_URL = "http://localhost:8082/deploymentlog-service";
    private static final String TEST_SYSTEM_NAME = "JME-IT-" + UUID.randomUUID();

    @BeforeAll
    static void startServices() throws Exception {
        startService(SERVICE_BASE_URL);
    }

    @Test
    void registerNewDeployment() {
        String deploymentId = UUID.randomUUID().toString();
        given()
                .baseUri(SERVICE_BASE_URL)
                .auth().preemptive().basic("write", "secret")
                .contentType(ContentType.JSON)
                .body("""
                        {
                            "startedAt": "2025-01-05T05:59:53.627Z",
                            "startedBy": "User1",
                            "environmentName": "DEV",
                            "componentVersion": {
                                "versionName": "1.2.70-5",
                                "taggedAt": "2022-04-08T07:09:03.627Z",
                                "versionControlUrl": "string",
                                "commitRef": "string",
                                "commitedAt": "2022-02-08T07:09:03.627Z",
                                "publishedVersion": true,
                                "componentName": "TestComponent",
                                "systemName": "%s"
                            },
                            "deploymentUnit": {
                                "type": "DOCKER_IMAGE",
                                "coordinates": "string",
                                "artifactRepositoryUrl": "string"
                            }
                        }
                        """.formatted(TEST_SYSTEM_NAME))
                .when()
                .put("/api/deployment/" + deploymentId)
                .then()
                .statusCode(HttpStatus.CREATED.value());

        String deploymentInfo = given()
                .baseUri(SERVICE_BASE_URL)
                .auth().preemptive().basic("read", "secret")
                .when()
                .get("/api/deployment/" + deploymentId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().body().asString();

        assertThat(deploymentInfo)
                .contains(TEST_SYSTEM_NAME)
                .contains(deploymentId);

    }
}
