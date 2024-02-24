package fr.epita.assistants.jws.converter;

import fr.epita.assistants.jws.data.model.GameModel;
import fr.epita.assistants.jws.converter.PlayerConvert;
import fr.epita.assistants.jws.data.model.PlayerModel;
import fr.epita.assistants.jws.domain.entity.GameEntity;
import fr.epita.assistants.jws.domain.entity.PlayerEntity;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class GameConvert {
    public GameEntity to_entity(GameModel gameModel)
    {
        GameEntity GameEntity = new GameEntity();
        GameEntity.starttime = gameModel.starttime;
        GameEntity.state = gameModel.state;
        GameEntity.id = gameModel.id;
        GameEntity.game_map = gameModel.game_map;
        return GameEntity;
    }

    public GameModel to_model(GameEntity gameEntity)
    {
        GameModel gameModel = new GameModel();
        gameModel.id = gameEntity.id;
        gameModel.game_map = gameEntity.game_map;
        //gameModel.game_player = gameEntity.game_player;

        //PlayerConvert conv = new PlayerConvert();
        //for (PlayerEntity pl : gameEntity.players) {
          //  gameModel.players.add(conv.to_model(pl));
        //}

        gameModel.starttime = gameEntity.starttime;
        gameModel.state = gameEntity.state;
        return gameModel;
    }
}
