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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Log
public class TasksTest {
    private HttpClient httpClient;

    @BeforeEach
    void setUp() {
        httpClient = HttpsClients.getNewClient();
    }

    @Test
    public void testTaskCanBeCreated() throws URISyntaxException, IOException, InterruptedException {
        var taskUUID = UUID.randomUUID().toString();
        var content = """
                {
                   "content":"{0}"
                }
                """.formatted(taskUUID);
        HttpRequest getCreateTaskRequest = ApiRequests.createNewTask(content);
        ApiClient apiClient = new ApiClient(httpClient);
        var httpResponse = apiClient.sendRequest(getCreateTaskRequest);
        assertEquals(httpResponse.statusCode(), 200, "Expected status code is 200");
    }

}

