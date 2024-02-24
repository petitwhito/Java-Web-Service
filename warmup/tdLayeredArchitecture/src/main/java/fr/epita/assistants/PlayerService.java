package fr.epita.assistants;

import java.util.ArrayList;
import java.util.List;

public class PlayerService {
    List<Player> players;

    public PlayerService() {
        this.players = new ArrayList<>();
    }

    public void addPlayer(Player player) {
        players.add(player);
    }
}
