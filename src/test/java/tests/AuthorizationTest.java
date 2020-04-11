package tests;

import lombok.extern.java.Log;
import net.ApiClient;
import net.ApiRequests;
import net.HttpsClients;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Log
public class AuthorizationTest {
    private HttpClient httpClient;

    @BeforeEach
    void setUp() {
        httpClient = HttpsClients.getNewClient();
    }

    @Test
    public void testAuthorizationCanBePerformed() throws URISyntaxException, IOException, InterruptedException {
        HttpRequest getProjectsRequest = ApiRequests.getProjects();
        ApiClient apiClient = new ApiClient(httpClient);
        var httpResponse = apiClient.sendRequest(getProjectsRequest);
        assertEquals(httpResponse.statusCode(), 200, "Expected status code is 200");
    }

}
