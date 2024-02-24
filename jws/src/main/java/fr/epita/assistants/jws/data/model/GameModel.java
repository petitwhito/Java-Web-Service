package fr.epita.assistants.jws.data.model;


import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.Value;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "game")
public class GameModel {
    public Timestamp starttime;
    public String state;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    //@OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    //public List<PlayerModel> players;


    public  @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "game_map", joinColumns = @JoinColumn(name = "gamemodel_id")) List<String> game_map;

    //@ElementCollection
    //@CollectionTable(name = "game_player", joinColumns = @JoinColumn(name = "gamemodel_id"))
    //@Column(name = "players_id")
    //public List<Long> game_player;
}
