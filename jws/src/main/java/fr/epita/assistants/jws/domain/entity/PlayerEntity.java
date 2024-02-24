package fr.epita.assistants.jws.domain.entity;

import fr.epita.assistants.jws.data.model.GameModel;
import lombok.Value;

import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import java.sql.Timestamp;

public class PlayerEntity {
    public Long id;
    public GameEntity game;
    public Timestamp lastbomb;
    public Timestamp lastmovement;
    public int lives;
    public String name;
    public int posX;
    public int posY;
    public int position;
}
