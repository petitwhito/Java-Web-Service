package fr.epita.assistants.jws.domain.entity;

import fr.epita.assistants.jws.data.model.PlayerModel;
import fr.epita.assistants.jws.utils.GameState;
import lombok.Value;

import java.sql.Timestamp;
import java.util.List;


public class GameEntity {
    public Timestamp starttime;
    public String state;
    public Long id;
    public List<PlayerEntity> players;
    public List<String> game_map;
    //public List<String> game_player;
}
