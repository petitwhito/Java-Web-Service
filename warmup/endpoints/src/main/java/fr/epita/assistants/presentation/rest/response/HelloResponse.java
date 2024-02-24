package fr.epita.assistants.presentation.rest.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HelloResponse {
    public String content;

    public HelloResponse()
    {
        this.content = null;
    }

    public HelloResponse(String content) {
        this.content = content;
    }
}
