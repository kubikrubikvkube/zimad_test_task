package tests;

import lombok.Getter;

@Getter
public enum Endpoint {
    PROJECTS("https://api.todoist.com/rest/v1/projects"),
    TASKS("https://api.todoist.com/rest/v1/tasks");

    private final String endpointUrl;

    Endpoint(String endpointUrl) {
        this.endpointUrl = endpointUrl;
    }
}
