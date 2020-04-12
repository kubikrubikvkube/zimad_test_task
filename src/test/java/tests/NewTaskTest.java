package tests;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.TaskDTO;
import lombok.extern.java.Log;
import net.ApiClient;
import net.ApiRequests;
import net.HttpsClients;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static net.ApiRequests.createNewTaskRequest;
import static net.ApiRequests.getActiveTasksRequest;
import static org.junit.jupiter.api.Assertions.*;

@Log
public class NewTaskTest {
    private static ObjectMapper objectMapper;
    private ApiClient apiClient;

    @BeforeAll
    static void beforeAll() {
        objectMapper = DefaultObjectMapper.getMapper();
    }

    @BeforeEach
    void setUp() {
        apiClient = new ApiClient(HttpsClients.getNewClient());
    }

    @Test
    @DisplayName("New task with default parameters should be created via API. It should be present in 'Active Tasks' list")
    public void newTaskIsPresentInActiveTasks() throws IOException {
        var contentText = "ZiMAD task with UUID %s".formatted(UUID.randomUUID().toString());
        var contentRequest = """
                {
                   "content":"%s"
                }
                """.formatted(contentText);

        var httpResponse = apiClient.sendRequest(createNewTaskRequest(contentRequest));
        assertEquals(httpResponse.statusCode(), 200);
        assertNotNull(httpResponse.body());
        TaskDTO createdTask = objectMapper.readerFor(TaskDTO.class).readValue(httpResponse.body());
        var createdTaskId = createdTask.getId();
        checkCreatedDefaultTask(createdTask, contentText);

        HttpResponse<String> activeTasksResponse = apiClient.sendRequest(getActiveTasksRequest());
        assertEquals(activeTasksResponse.statusCode(), 200, "Expected status code is 200");
        assertNotNull(activeTasksResponse.body());
        List<TaskDTO> activeTasks = objectMapper.readValue(activeTasksResponse.body(), new TypeReference<>() {
        });
        Optional<TaskDTO> taskFromApiOpt = activeTasks.stream().filter(task -> task.getId().equals(createdTaskId)).findAny();
        assertTrue(taskFromApiOpt.isPresent());
        TaskDTO taskFromApi = taskFromApiOpt.get();
        checkCreatedDefaultTask(taskFromApi, contentText);
        assertEquals(createdTask, taskFromApi);
    }

    @ParameterizedTest
    @DisplayName("All positive test cases should be passed. Implemented only context-unaware cases.")
    @CsvFileSource(resources = "TaskTestsPositiveCases.csv", numLinesToSkip = 1)
    public void allPositiveTestCasesShouldBePassed(String content, Integer order, Integer priority, String due_lang) throws IOException {
        HttpRequest request = ApiRequests.createNewTaskRequestBuilder()
                .content(content)
                .order(order)
                .priority(priority)
                .due_lang(due_lang)
                .build();

        var httpResponse = apiClient.sendRequest(request);
        assertEquals(httpResponse.statusCode(), 200);
        assertNotNull(httpResponse.body());
        TaskDTO createdTask = objectMapper.readerFor(TaskDTO.class).readValue(httpResponse.body());
        checkCreatedDefaultTask(createdTask, content);
    }

    private void checkCreatedDefaultTask(TaskDTO createdTask, String contentText) {
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

