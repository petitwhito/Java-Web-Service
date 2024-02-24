package fr.epita.assistants.jws.converter;

import fr.epita.assistants.jws.data.model.GameModel;
import fr.epita.assistants.jws.data.model.PlayerModel;
import fr.epita.assistants.jws.data.repository.PlayerRepository;
import fr.epita.assistants.jws.domain.entity.GameEntity;
import fr.epita.assistants.jws.domain.entity.PlayerEntity;
import fr.epita.assistants.jws.converter.PlayerConvert;

import java.util.ArrayList;
import java.util.List;




public class PlayerConvert {

    public PlayerEntity to_entity(PlayerModel playerModel)
    {
        GameConvert gm = new GameConvert();
        PlayerEntity pe =  new PlayerEntity();
        pe.id = playerModel.id;
        pe.lives = playerModel.lives;
        pe.name  = playerModel.name;
        pe.posY = playerModel.posY;
        pe.posX = playerModel.posX;
        pe.lastbomb = playerModel.lastbomb;
        pe.lastmovement = playerModel.lastmovement;

        pe.game = gm.to_entity(playerModel.game);
        pe.game.players = new ArrayList<>();

        pe.position = playerModel.position;
        return pe;
    }

    public PlayerModel to_model(PlayerEntity playerEntity)
    {
        PlayerModel playerModel = new PlayerModel();
        GameConvert gm = new GameConvert();
        playerModel.game = gm.to_model(playerEntity.game);
        playerModel.lastbomb = playerEntity.lastbomb;
        playerModel.lives = playerEntity.lives;
        playerModel.id = playerEntity.id;
        playerModel.posX = playerEntity.posX;
        playerModel.posY = playerEntity.posY;
        playerModel.position = playerEntity.position;
        playerModel.lastmovement = playerEntity.lastmovement;
        playerModel.name = playerEntity.name;
        return playerModel;
    }

}
