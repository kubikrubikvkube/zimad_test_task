package tests;

import lombok.extern.java.Log;
import net.HttpsClients;
import net.TodoistRequests;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Log
public class AuthorizationTest {
    private HttpClient httpClient;

    @BeforeEach
    void setUp() {
        httpClient = HttpsClients.getNewClient();
    }

    @Test
    public void testAuthorizationCanBePerformed() throws URISyntaxException, IOException, InterruptedException {
        HttpRequest getProjectsRequest = TodoistRequests.getProjects();
        HttpResponse<String> httpResponse = httpClient.send(getProjectsRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(httpResponse.statusCode(), 200, "Expected status code is 200");
        String responseBody = httpResponse.body();
        assertNotNull(responseBody, "Response should contain body");
        log.info("Response body is " + responseBody);
    }

}
