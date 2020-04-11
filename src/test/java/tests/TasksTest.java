package tests;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.TaskDTO;
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
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Log
public class TasksTest {
    private HttpClient httpClient;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        httpClient = HttpsClients.getNewClient();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testTaskCanBeCreated() throws URISyntaxException, IOException, InterruptedException {
        ApiClient apiClient = new ApiClient(httpClient);
        var taskUUID = UUID.randomUUID().toString();
        var contentText = "ZiMAD task with UUID %s".formatted(taskUUID);
        var contentRequest = """
                {
                   "content":"%s"
                }
                """.formatted(contentText);
        HttpRequest getCreateTaskRequest = ApiRequests.createNewTask(contentRequest);
        var httpResponse = apiClient.sendRequest(getCreateTaskRequest);
        assertEquals(httpResponse.statusCode(), 200, "Expected status code is 200");
        TaskDTO createdTask = objectMapper.readerFor(TaskDTO.class).readValue(httpResponse.body());
        var createdTaskId = createdTask.getId();
        assertNotNull(createdTaskId);
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
        HttpRequest getActiveTasks = ApiRequests.getActiveTasks();
        HttpResponse<String> activeTasksResponse = apiClient.sendRequest(getActiveTasks);
        assertEquals(activeTasksResponse.statusCode(), 200, "Expected status code is 200");
        List<TaskDTO> activeTasks = objectMapper.readValue(activeTasksResponse.body(), new TypeReference<>() {
        });
        Optional<TaskDTO> createdTaskFromActiveTasksOpt = activeTasks.stream().filter(task -> task.getId().equals(createdTaskId)).findAny();
        assertTrue(createdTaskFromActiveTasksOpt.isPresent());
        TaskDTO taskDTO = createdTaskFromActiveTasksOpt.get();
        assertEquals(createdTask, taskDTO);
    }

}

