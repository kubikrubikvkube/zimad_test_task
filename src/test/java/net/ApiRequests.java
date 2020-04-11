package net;

import authorization.AuthorizationCredentials;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import tests.Endpoint;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

import static java.text.MessageFormat.format;

@UtilityClass
public class ApiRequests {
    public static HttpRequest getProjects() {
        var projectsEndpointUri = getUriFromEndpoint(Endpoint.PROJECTS);
        return HttpRequest.newBuilder()
                .GET()
                .uri(projectsEndpointUri)
                .header("Authorization", "Bearer " + AuthorizationCredentials.API_TOKEN)
                .build();
    }

    public static HttpRequest createNewTask(@NonNull String content) {
        return createNewTask(content, null);
    }

    public static HttpRequest createNewTask(@NonNull String content, Map<String, String> parameters) {
        var tasksEndpointURI = getUriFromEndpoint(Endpoint.TASKS);
        var requestUUID = UUID.randomUUID().toString();
        return HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(content, StandardCharsets.UTF_8))
                .uri(tasksEndpointURI)
                .header("Authorization", "Bearer " + AuthorizationCredentials.API_TOKEN)
                .header("Content-Type", "application/json")
                .header("X-Request-Id", requestUUID)
                .build();
    }

    public static HttpRequest getActiveTasks() {
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
