package tests;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.TaskDTO;
import lombok.extern.java.Log;
import net.ApiClient;
import net.HttpsClients;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static net.ApiRequests.createNewTask;
import static net.ApiRequests.getActiveTasks;
import static org.junit.jupiter.api.Assertions.*;

@Log
public class TasksTest {
    private static ObjectMapper objectMapper;
    private ApiClient apiClient;

    @BeforeAll
    static void beforeAll() {
        objectMapper = new ObjectMapper();
    }

    @BeforeEach
    void setUp() {
        apiClient = new ApiClient(HttpsClients.getNewClient());
    }

    @Test
    public void testTaskCanBeCreated() throws URISyntaxException, IOException, InterruptedException {
        var contentText = "ZiMAD task with UUID %s".formatted(UUID.randomUUID().toString());
        var contentRequest = """
                {
                   "content":"%s"
                }
                """.formatted(contentText);

        var httpResponse = apiClient.sendRequest(createNewTask(contentRequest));
        assertEquals(httpResponse.statusCode(), 200);
        assertNotNull(httpResponse.body());
        TaskDTO createdTask = objectMapper.readerFor(TaskDTO.class).readValue(httpResponse.body());
        var createdTaskId = createdTask.getId();
        checkCreatedTask(createdTask, contentText);

        HttpResponse<String> activeTasksResponse = apiClient.sendRequest(getActiveTasks());
        assertEquals(activeTasksResponse.statusCode(), 200, "Expected status code is 200");
        assertNotNull(activeTasksResponse.body());
        List<TaskDTO> activeTasks = objectMapper.readValue(activeTasksResponse.body(), new TypeReference<>() {
        });
        Optional<TaskDTO> taskFromApiOpt = activeTasks.stream().filter(task -> task.getId().equals(createdTaskId)).findAny();
        assertTrue(taskFromApiOpt.isPresent());
        TaskDTO taskFromApi = taskFromApiOpt.get();
        checkCreatedTask(taskFromApi, contentText);
        assertEquals(createdTask, taskFromApi);
    }

    private void checkCreatedTask(TaskDTO createdTask, String contentText) {
        assertNotNull(createdTask.getId());
        assertNotNull(createdTask.getProject_id());
        assertNotNull(createdTask.getSection_id());
        assertNotNull(createdTask.getOrder());
        assertEquals(createdTask.getContent(), contentText);
        assertFalse(createdTask.getCompleted());
        assertNotNull(createdTask.getLabel_ids());
        assertNotNull(createdTask.getPriority());
        assertEquals(createdTask.getComment_count(), 0);
        assertNotNull(createdTask.getCreated());
        assertEquals(createdTask.getUrl(), "https://todoist.com/showTask?id=" + createdTask.getId());
    }
}

