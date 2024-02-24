package fr.epita.assistants.jws.presentation.rest;

import fr.epita.assistants.jws.data.model.GameModel;
import fr.epita.assistants.jws.data.model.PlayerModel;
import fr.epita.assistants.jws.data.repository.GameRepository;
import fr.epita.assistants.jws.domain.entity.GameEntity;
import fr.epita.assistants.jws.domain.entity.PlayerEntity;
import fr.epita.assistants.jws.domain.service.GameService;
import fr.epita.assistants.jws.presentation.rest.request.CreateGameRequest;
import fr.epita.assistants.jws.presentation.rest.request.JoinGameRequest;
import fr.epita.assistants.jws.presentation.rest.request.MovePlayerRequest;
import fr.epita.assistants.jws.presentation.rest.response.GameDetailResponse;
import fr.epita.assistants.jws.presentation.rest.response.GameListResponse;
import fr.epita.assistants.jws.utils.CustomException;
import fr.epita.assistants.jws.utils.GameState;
import fr.epita.assistants.jws.utils.PlayerDetail;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/")
public class Endpoint {
    @Inject GameService gs;


    @Path("/games")
    @GET
    public List<GameListResponse> GameList() {
        List<GameListResponse> res = new ArrayList<>();
        for (GameEntity e : gs.ListGame()) {
            GameListResponse resp = new GameListResponse();
            resp.id = e.id;
            resp.players = e.players.size();
            resp.state = e.state;
            res.add(resp);
        }
        return res;
    }

    @Path("/games")
    @POST
    public GameDetailResponse Creategame(CreateGameRequest r) {
        GameEntity ge = gs.CreateGame(r.name);

        //Convert to GameDetail to move to service
        GameDetailResponse response = new GameDetailResponse();
        response.state = "STARTING";
        response.id = ge.id;
        response.map = ge.game_map;
        response.startTime = ge.starttime.toString();

        PlayerDetail pd = new PlayerDetail();
        pd.id = ge.players.get(0).id;
        pd.lives = ge.players.get(0).lives;
        pd.name = ge.players.get(0).name;
        pd.posX = ge.players.get(0).posX;
        pd.posY = ge.players.get(0).posY;

        response.players = new ArrayList<>();
        response.players.add(pd);

        return response;
    }

    @Path("/games/{gameId}")
    @GET
    public GameDetailResponse GetGameInfo(@PathParam("gameId") Long gameId)
    {
        GameEntity ge = gs.GetInfo(gameId);
        GameDetailResponse response = new GameDetailResponse();
        response.state = ge.state;
        response.id = ge.id;
        response.map = ge.game_map;
        response.startTime = ge.starttime.toString();
        response.players = new ArrayList<>();

        //Boucler ici sur les players
        for (PlayerEntity pm: ge.players) {
            PlayerDetail pd = new PlayerDetail();
            pd.id = pm.id;
            pd.lives = pm.lives;
            pd.name =pm.name;
            pd.posX = pm.posX;
            pd.posY = pm.posY;
            response.players.add(pd);
        }


        return response;
    }

    @Path("/games/{gameId}")
    @POST
    public GameDetailResponse JoinGame(@PathParam("gameId") Long gameId, JoinGameRequest joinGameRequest)
    {
        GameEntity ge = gs.AddPlayer(gameId, joinGameRequest.name);
        GameDetailResponse response = new GameDetailResponse();
        response.state = ge.state;
        response.id = ge.id;
        response.map = ge.game_map;
        response.startTime = ge.starttime.toString();

        //Boucler ici sur les players
        response.players = new ArrayList<>();

        //Boucler ici sur les players
        for (PlayerEntity pm: ge.players) {
            PlayerDetail pd = new PlayerDetail();
            pd.id = pm.id;
            pd.lives = pm.lives;
            pd.name =pm.name;
            pd.posX = pm.posX;
            pd.posY = pm.posY;
            response.players.add(pd);
        }

        return response;
    }

    @Path("/games/{gameId}/start")
    @PATCH
    public GameDetailResponse StartGame(@PathParam("gameId") Long gameId)
    {
        GameEntity ge = gs.StartGame(gameId);
        GameDetailResponse response = new GameDetailResponse();
        response.state = ge.state;
        response.id = ge.id;
        response.map = ge.game_map;
        response.startTime = ge.starttime.toString();

        //Boucler ici sur les players
        response.players = new ArrayList<>();

        //Boucler ici sur les players
        for (PlayerEntity pm: ge.players) {
            PlayerDetail pd = new PlayerDetail();
            pd.id = pm.id;
            pd.lives = pm.lives;
            pd.name =pm.name;
            pd.posX = pm.posX;
            pd.posY = pm.posY;
            response.players.add(pd);
        }

        return response;
    }

    @Path("/games/{gameId}/players/{playerId}/bomb")
    @POST
    public Response MoveBomb(@PathParam("gameId") Long gameId, @PathParam("playerId") Long playerId, MovePlayerRequest movePlayerRequest)
    {
        if (movePlayerRequest == null)
            throw new BadRequestException();
        try {
            GameEntity ge = gs.MoveBomb(gameId, playerId, movePlayerRequest.posX, movePlayerRequest.posY);
            GameDetailResponse response = new GameDetailResponse();
            response.state = ge.state;
            response.id = ge.id;
            response.map = ge.game_map;
            response.startTime = ge.starttime.toString();

            //Boucler ici sur les players
            response.players = new ArrayList<>();

            //Boucler ici sur les players
            for (PlayerEntity pm: ge.players) {
                PlayerDetail pd = new PlayerDetail();
                pd.id = pm.id;
                pd.lives = pm.lives;
                pd.name =pm.name;
                pd.posX = pm.posX;
                pd.posY = pm.posY;
                response.players.add(pd);
            }

            return Response.ok(response).build();
        } catch (CustomException e) {
            return Response.status(e.getReturnCode()).build();
        }
    }

    @Path("/games/{gameId}/players/{playerId}/move")
    @POST
    public Response MovePlayer(@PathParam("gameId") Long gameId, @PathParam("playerId") Long playerId, MovePlayerRequest movePlayerRequest)
    {
        if (movePlayerRequest == null)
            throw new BadRequestException();
        try {
            GameEntity ge = gs.MovePlayer(gameId, playerId, movePlayerRequest.posX, movePlayerRequest.posY);
            GameDetailResponse response = new GameDetailResponse();
            response.state = ge.state;
            response.id = ge.id;
            response.map = ge.game_map;
            response.startTime = ge.starttime.toString();

            //Boucler ici sur les players
            response.players = new ArrayList<>();

            //Boucler ici sur les players
            for (PlayerEntity pm: ge.players) {
                PlayerDetail pd = new PlayerDetail();
                pd.id = pm.id;
                pd.lives = pm.lives;
                pd.name =pm.name;
                pd.posX = pm.posX;
                pd.posY = pm.posY;
                response.players.add(pd);
            }

            return Response.ok(response).build();
        } catch (CustomException e) {
            return Response.status(e.getReturnCode()).build();
        }

    }
}