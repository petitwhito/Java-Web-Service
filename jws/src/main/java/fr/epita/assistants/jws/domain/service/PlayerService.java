package fr.epita.assistants.jws.domain.service;

import fr.epita.assistants.jws.data.model.PlayerModel;
import fr.epita.assistants.jws.domain.entity.PlayerEntity;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class PlayerService {
    public List<PlayerEntity> players;
}
