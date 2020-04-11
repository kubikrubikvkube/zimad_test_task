package tests;

import lombok.Getter;

@Getter
public enum Endpoint {
    PROJECTS("https://api.todoist.com/rest/v1/projects");

    private final String endpointUrl;

    Endpoint(String endpointUrl) {
        this.endpointUrl = endpointUrl;
    }
}
