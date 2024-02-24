package fr.epita.assistants;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;

import javax.enterprise.event.Observes;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Produces(MediaType.APPLICATION_JSON)
@Path("/piece")

public class Endpoint {
    private Piece piece = new Piece(0, 0);

    @PUT
    @Path("/move/{direction}")
    public Piece move(@PathParam("direction") String direction,@QueryParam("distance") int distance )
    {
        switch (direction)
        {
            case "up-left":
                piece.moveUpLeft();
                return piece;

            case "up-right":
                piece.moveUpRight();
                return piece;
            case "down-left":
                piece.moveDownLeft();
                return piece;
            default:
                piece.moveDownRight();
                return piece;
        }
    }

    /*
    @PUT
    @Path("up-left")
    public Piece moveUpLeft(@QueryParam("distance") int distance) {
        piece.moveUpLeft();
        return piece;
    }

    @PUT
    @Path("up-right")
    public Piece moveUpRight(@QueryParam("distance") int distance) {
        piece.moveUpRight();
        return piece;
    }

    @PUT
    @Path("down-left")
    public Piece moveDownLeft(@QueryParam("distance") int distance) {
        piece.moveDownLeft();
        return piece;
    }

    @PUT
    @Path("down-right")
    public Piece moveDownRight(@QueryParam("distance") int distance) {
        piece.moveDownRight();
        return piece;
    }
     */

    @POST
    @Path("create")
    public Piece create() {
        piece = new Piece(0, 0);
        return piece;
    }

    @POST
    @Path("player/create/{name}")
    public Player createPlayer(@QueryParam("name") String name)
    {
        Player player = new Player(name);
        player.pieces.add(piece);
        return player;
    }

    void onStartup(@Observes StartupEvent ev) {
        // This method is executed at startup
        // You might want to use it for the Step 4 in order to load the in-memory database.
    }

    void onStop(@Observes ShutdownEvent ev) {
        // This method is executed at shutdown
        // You might want to use it for the Step 4 in order to save the in-memory database.
    }
}
