package fr.epita.assistants.jws.presentation.rest.response;

import fr.epita.assistants.jws.data.model.PlayerModel;
import fr.epita.assistants.jws.utils.GameState;
import fr.epita.assistants.jws.utils.PlayerDetail;

import java.util.ArrayList;

import java.util.List;



public class GameDetailResponse {
    public String startTime;
    public String state;
    public List<PlayerDetail> players;

    public List<String> map = new ArrayList<>();
    public Long id;

}
