package net;

import authorization.AuthorizationCredentials;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.CreateNewTaskRequestDTO;
import lombok.Builder;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.java.Log;
import tests.DefaultObjectMapper;
import tests.Endpoint;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

import static java.text.MessageFormat.format;

@UtilityClass
@Log
public class ApiRequests {
    private static final ObjectMapper objectMapper = DefaultObjectMapper.getMapper();

    public static HttpRequest getProjectsRequest() {
        var projectsEndpointUri = getUriFromEndpoint(Endpoint.PROJECTS);
        return HttpRequest.newBuilder()
                .GET()
                .uri(projectsEndpointUri)
                .header("Authorization", "Bearer " + AuthorizationCredentials.API_TOKEN)
                .build();
    }

    @SneakyThrows(value = {JsonProcessingException.class})
    @Builder(builderMethodName = "createNewTaskRequestBuilder")
    public static HttpRequest createNewTaskRequest(String content, Integer project_id, Integer section_id, Integer parent, Integer order, List<Integer> label_ids, Integer priority, String due_string, String due_date, String due_datetime, String due_lang) {
        var dtoBuilder = CreateNewTaskRequestDTO.builder();
        CreateNewTaskRequestDTO taskRequestDTO = dtoBuilder
                .content(content)
                .project_id(project_id)
                .section_id(section_id)
                .parent(parent)
                .order(order)
                .label_ids(label_ids)
                .priority(priority)
                .due_string(due_string)
                .due_date(due_date)
                .due_datetime(due_datetime)
                .due_lang(due_lang)
                .build();

        String requestString = objectMapper.writeValueAsString(taskRequestDTO);
        log.info("Created new task request " + requestString);

        var requestUUID = UUID.randomUUID().toString();
        return HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(requestString, StandardCharsets.UTF_8))
                .uri(getUriFromEndpoint(Endpoint.TASKS))
                .header("Authorization", "Bearer " + AuthorizationCredentials.API_TOKEN)
                .header("Content-Type", "application/json")
                .header("X-Request-Id", requestUUID)
                .build();
    }


    public static HttpRequest createNewTaskRequest(@NonNull String body) {
        var tasksEndpointURI = getUriFromEndpoint(Endpoint.TASKS);
        var requestUUID = UUID.randomUUID().toString();
        return HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8))
                .uri(tasksEndpointURI)
                .header("Authorization", "Bearer " + AuthorizationCredentials.API_TOKEN)
                .header("Content-Type", "application/json")
                .header("X-Request-Id", requestUUID)
                .build();
    }

    public static HttpRequest getActiveTasksRequest() {
        var tasksEndpointUri = getUriFromEndpoint(Endpoint.TASKS);
        return HttpRequest.newBuilder()
                .GET()
                .uri(tasksEndpointUri)
                .header("Authorization", "Bearer " + AuthorizationCredentials.API_TOKEN)
                .build();
    }

    private static URI getUriFromEndpoint(Endpoint endpoint) {
        var endPointUrl = endpoint.getEndpointUrl();
        try {
            return new URI(endPointUrl);
        } catch (URISyntaxException e) {
            throw new RuntimeException(format("Endpoint URL in {0} is invalid and cannot be matched to valid java.net.URI object. Invalid URL is {1}", endpoint.name(), endPointUrl));
        }
    }


}
