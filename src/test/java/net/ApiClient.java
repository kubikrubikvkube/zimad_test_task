package net;

import lombok.extern.java.Log;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Log
public class ApiClient {
    private final HttpClient apiClient;

    public ApiClient(HttpClient apiClient) {
        this.apiClient = apiClient;
    }

    public HttpResponse<String> sendRequest(HttpRequest request) throws IOException {
        log.info("Sending request " + request);
        try {
            return apiClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new IOException(String.format("Can't complete request %s to %s", request, request.uri()), e);
        }
    }
}
