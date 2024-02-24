package fr.epita.assistants.jws.data.model;

import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "player")
@Setter
public class PlayerModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    //@ManyToOne
    //@JoinColumn(name = "game_id")
    //public GameModel Game;

    @ManyToOne
    @JoinTable( name = "game_player",
            joinColumns = @JoinColumn( name = "players_id" ),
            inverseJoinColumns = @JoinColumn( name = "gamemodel_id" ) )
    //@JoinColumn(name = "game_id")
    public GameModel game;

    public Timestamp lastbomb;
    public Timestamp lastmovement;
    public int lives;
    public String name;
    public int posX;
    public int posY;
    public int position;

}
