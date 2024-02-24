package fr.epita.assistants.presentation.rest;

import fr.epita.assistants.presentation.rest.request.ReverseRequest;
import fr.epita.assistants.presentation.rest.response.HelloResponse;
import fr.epita.assistants.presentation.rest.response.ReverseResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Produces(MediaType.APPLICATION_JSON)
@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
public class Endpoints {

    @Path("/hello/{name}")
    @GET
    public HelloResponse hello(@PathParam("name") String name)
    {
        if (name == null)
            throw new BadRequestException();
        return new HelloResponse("hello " + name);
    }

    @Path("/reverse")
    @POST
    public ReverseResponse reverse(ReverseRequest request)
    {
        if (request == null || request.content == null || request.content.isEmpty())
            throw new BadRequestException();
        return new ReverseResponse(request);
    }
}
