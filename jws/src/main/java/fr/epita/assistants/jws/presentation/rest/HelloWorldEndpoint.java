package fr.epita.assistants.jws.presentation.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
@Produces(MediaType.TEXT_PLAIN)
@Consumes(MediaType.TEXT_PLAIN)
public class HelloWorldEndpoint {
    @GET @Path("/")
    public String helloWorld() {
        return "Hello World!";
    }

    @GET @Path("/{name}")
    public String helloWorld(@PathParam("name") String name) {
        return "Hello " + name + "!";
    }
}
