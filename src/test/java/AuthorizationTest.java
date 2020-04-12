import lombok.extern.java.Log;
import net.ApiClient;
import net.ApiRequests;
import net.HttpsClients;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
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
    @DisplayName("Authorization header should be processed by API and return code should be 200")
    public void testAuthorizationCanBePerformed() throws IOException {
        HttpRequest getProjectsRequest = ApiRequests.getProjectsRequest();
        ApiClient apiClient = new ApiClient(httpClient);
        var httpResponse = apiClient.sendRequest(getProjectsRequest);
        assertEquals(httpResponse.statusCode(), 200, "Expected status code is 200");
    }

}
