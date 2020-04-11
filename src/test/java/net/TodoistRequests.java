package net;

import authorization.AuthorizationCredentials;
import tests.Endpoint;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;

import static java.text.MessageFormat.format;

public class TodoistRequests {

    public static HttpRequest getProjects() {
        var projectsEndpointUri = getUriFromEndpoint(Endpoint.PROJECTS);
        return HttpRequest.newBuilder()
                .GET()
                .uri(projectsEndpointUri)
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
