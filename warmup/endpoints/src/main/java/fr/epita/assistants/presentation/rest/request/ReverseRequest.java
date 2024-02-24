package fr.epita.assistants.presentation.rest.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReverseRequest {
    public String content;

    public ReverseRequest() {
        this.content = null;
    }

    public ReverseRequest(String content)
    {
        this.content = content;
    }
}
