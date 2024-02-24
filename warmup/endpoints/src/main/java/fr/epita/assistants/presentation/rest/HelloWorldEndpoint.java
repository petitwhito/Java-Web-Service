package fr.epita.assistants.presentation.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
@Consumes(MediaType.TEXT_PLAIN)
@Produces(MediaType.TEXT_PLAIN)
public class HelloWorldEndpoint {
    @Path("/")
    @GET
    public String helloWorld() {
        return "Hello World!";
    }
}
