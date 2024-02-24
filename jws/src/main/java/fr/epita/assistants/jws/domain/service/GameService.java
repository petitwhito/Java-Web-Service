package fr.epita.assistants.jws.domain.service;


import fr.epita.assistants.jws.converter.GameConvert;
import fr.epita.assistants.jws.converter.PlayerConvert;
import fr.epita.assistants.jws.data.model.GameModel;
import fr.epita.assistants.jws.data.model.PlayerModel;
import fr.epita.assistants.jws.data.repository.GameRepository;
import fr.epita.assistants.jws.data.repository.PlayerRepository;
import fr.epita.assistants.jws.domain.entity.GameEntity;
import fr.epita.assistants.jws.domain.entity.PlayerEntity;
import fr.epita.assistants.jws.utils.CustomException;
import fr.epita.assistants.jws.utils.GameState;
import net.bytebuddy.implementation.bytecode.Throw;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jsoup.Connection;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.swing.text.html.parser.Entity;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;

@ApplicationScoped
public class GameService {
    List<GameEntity> gameList;
    @ConfigProperty(name = "JWS_MAP_PATH")
    String path;

    @ConfigProperty(name = "JWS_TICK_DURATION")
    Long duration;

    @ConfigProperty(name = "JWS_DELAY_MOVEMENT")
    Long movement_time;

    @ConfigProperty(name = "JWS_DELAY_BOMB")
    Long delay_bomb_time;

    @ConfigProperty(name = "JWS_DELAY_FREE")
    Long free_time;

    @ConfigProperty(name = "JWS_DELAY_SHRINK")
    Long delay_shrink;

    GameRepository Gamerepo = new GameRepository();

    PlayerRepository Playerrepo = new PlayerRepository();
    GameConvert GameConverter = new GameConvert();
    PlayerConvert PlayerConverter = new PlayerConvert();



    @Transactional
    public GameEntity CreateGame(String name_player) {
        if (Objects.equals(name_player, "") || name_player == null || name_player.equals(""))
        {
            throw new BadRequestException();
        }
        GameModel gm = new GameModel();
        gm.state = "STARTING";
        gm.game_map = new ArrayList<>();

        try {
            //System.out.println(path);
            BufferedReader bf = new BufferedReader(
                    new FileReader(path));
            String line = bf.readLine();

            while (line != null) {
                gm.game_map.add(line);
                line = bf.readLine();
            }
            gm.starttime = Timestamp.from(Instant.now());

            bf.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        PlayerModel pm = new PlayerModel();

        pm.name = name_player;
        pm.game = gm;
        pm.posY = 1;
        pm.posX = 1;
        pm.lives = 3;
        pm.position = 1;
        pm.lastbomb = new Timestamp(0);
        pm.lastmovement = new Timestamp(0);

        Gamerepo.persist(gm);
        Playerrepo.persist(pm);

        GameEntity t = GameConverter.to_entity(gm);
        t.players = new ArrayList<>();
        t.players.add(PlayerConverter.to_entity(pm));

        return t;
    }

    @Transactional
    public List<GameEntity> ListGame()
    {
        List<GameEntity> res = new ArrayList<>();
        List<GameModel> gm = Gamerepo.listAll();
        for (GameModel m: gm) {
            GameEntity ent = GameConverter.to_entity(m);
            List<PlayerModel> models = Playerrepo.find("game", m).list();
            ent.players = new ArrayList<>();
            for (PlayerModel pm : models) {
                ent.players.add(PlayerConverter.to_entity(pm));
            }
            res.add(ent);
        }
        return res;
    }

public GameEntity GetInfo(Long game_id)
{
    GameModel gm = Gamerepo.findById(game_id);
    if (gm == null)
        throw new NotFoundException();
    GameEntity ent = GameConverter.to_entity(gm);
    List<PlayerModel> models = Playerrepo.find("game", gm).list();
    ent.players = new ArrayList<>();
    for (PlayerModel pm : models) {
        ent.players.add(PlayerConverter.to_entity(pm));
    }
    return ent;
}

@Transactional
public GameEntity AddPlayer(Long game_id, String player_name)
{
    GameModel gm = Gamerepo.findById(game_id);
    if (gm == null)
        throw new NotFoundException();
    List<PlayerModel> models = Playerrepo.find("game", gm).list();
    PlayerModel pm = new PlayerModel();

    pm.name = player_name;
    pm.game = gm;
    pm.position = models.size() + 1;
    GameEntity ent = GameConverter.to_entity(gm);

    if (pm.position >= 5 || !ent.state.equals("STARTING") || !gm.state.equals("STARTING"))
    {
        throw new BadRequestException();
    }
    switch (pm.position)
    {
        case 2:
            pm.posY = 1;
            pm.posX = 15;
            break;
        case 3:
            pm.posX = 15;
            pm.posY = 13;
            break;
        case 4:
            pm.posX = 1;
            pm.posY = 13;
    }

    pm.lives = 3;
    pm.lastbomb = new Timestamp(0);
    pm.lastmovement = new Timestamp(0);
    models.add(pm);
    Playerrepo.persist(pm);

    ent.players = new ArrayList<>();
    for (PlayerModel l : models) {
        PlayerEntity d = PlayerConverter.to_entity(l);
        d.game.players.add(d);
        ent.players.add(d);
    }

    return ent;
}

@Transactional
public void Shrink(Long game_id, int index)
{
    GameModel gm = Gamerepo.findById(game_id);

    gm.game_map.set(index - 1, "9M8M"); // G
    int size = gm.game_map.size() - index;

    gm.game_map.set(size, "9M8M"); // G

    for (int i = 0; i < gm.game_map.size(); i++)
    {
        gm.game_map.set(i, runLengthDecode(gm.game_map.get(i)));
    }

    //System.out.println(gm.game_map);

    for (int i = index; i < gm.game_map.size() - index; i++)
    {
        StringBuilder b = new StringBuilder(gm.game_map.get(i));
        b.setCharAt(b.length() - index, 'M');
        b.setCharAt(index - 1, 'M');
        gm.game_map.set(i, b.toString());
    }


    List<PlayerModel> models = Playerrepo.find("game", gm).list();
    int count = 0;
    for (PlayerModel pm : models) {
        if (gm.game_map.get(pm.posY).charAt(pm.posX) == 'M')
            pm.lives = 0;
        else if (pm.lives > 0)
            count++;
    }

    for (int i = 0; i < gm.game_map.size(); i++)
    {
        gm.game_map.set(i, runLengthEncode(gm.game_map.get(i)));
    }

    //System.out.println(gm.game_map);

    if (count > 1)
        CompletableFuture.runAsync(() -> Shrink(game_id, index + 1), CompletableFuture.delayedExecutor(delay_shrink * duration, TimeUnit.MILLISECONDS));
    else
        gm.state = "FINISHED";

}

@Transactional
public GameEntity StartGame(Long game_id)
{
    GameModel gm = Gamerepo.findById(game_id);
    if (gm == null || game_id == null)
        throw new BadRequestException();

    if (!gm.state.equals("STARTING"))
        throw new NotFoundException();

    gm.state = "RUNNING";

    GameEntity ent = GameConverter.to_entity(gm);

    List<PlayerModel> models = Playerrepo.find("game", gm).list();
    ent.players = new ArrayList<>();

    for (PlayerModel l : models) {
        PlayerEntity d = PlayerConverter.to_entity(l);
        d.game.players.add(d);
        ent.players.add(d);
    }

    if (ent.players.size() == 1) {
        ent.state = "FINISHED";
        gm.state = "FINISHED";
    }

    CompletableFuture.runAsync(() -> Shrink(game_id, 2), CompletableFuture.delayedExecutor(free_time * duration, TimeUnit.MILLISECONDS));

    return ent;
}
@Transactional
public String runLengthDecode(String rle)
{
        StringBuilder result = new StringBuilder();
        char[] chars = rle.toCharArray();

        int count = 0;
        for (char c : chars) {
            if (Character.isDigit(c)) {
                count = 10 * count + Character.getNumericValue(c);
            } else {
                result.append(String.join("", Collections.nCopies(count, String.valueOf(c))));
                count = 0;
            }
        }
        return result.toString();
}

    @Transactional
    String runLengthEncode(String input) {
        StringBuilder result = new StringBuilder();
        int count = 1;
        char[] chars = input.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (i + 1 < chars.length && c == chars[i + 1] && count < 9) {
                count++;
            } else {
                result.append(count).append(c);
                count = 1;
            }
        }
        return result.toString();
    }

@Transactional
public GameEntity MoveBomb(Long gameId, Long playerId, int posX, int posY) throws CustomException {
    //Place above
    PlayerModel pm = Playerrepo.findById(playerId);
    if (pm == null || playerId == null)
    {
        throw new NotFoundException();
    }

    if (pm.lives == 0)
        throw new BadRequestException();

    GameModel gm = Gamerepo.findById(gameId);

    if (gm == null || gameId == null)
        throw new NotFoundException();

    if (gm.game_map == null || gm.game_map.size() < 15)
        throw new CustomException("message", 429);

    if (Math.abs(posX - pm.posX) == 1 && Math.abs(posY - pm.posY) == 1)
        throw new BadRequestException();

    if (Math.abs(posX - pm.posX) > 1)
        throw new BadRequestException();

    if (Math.abs(posY - pm.posY) > 1)
        throw new BadRequestException();

    if (!gm.state.equals("RUNNING"))
        throw new BadRequestException();

    if (posX != pm.posX || posY != pm.posY)
        throw new BadRequestException();

    GameEntity gameEntity = GameConverter.to_entity(gm);

    for (int i = 0; i < gameEntity.game_map.size(); i++)
    {
        gameEntity.game_map.set(i, runLengthDecode(gameEntity.game_map.get(i)));
    }

    String s = gameEntity.game_map.get(posY).substring(0, posX) + 'B' + gameEntity.game_map.get(posY).substring(posX + 1);
    gameEntity.game_map.set(posY, s);

    for (int i = 0; i < gameEntity.game_map.size(); i++)
    {
        gameEntity.game_map.set(i, runLengthEncode(gameEntity.game_map.get(i)));
    }

    Timestamp t = Timestamp.from(Instant.now());

    if (t.getTime() - pm.lastbomb.getTime() < delay_bomb_time * duration)
    {
        throw new CustomException("Custom", 429);
    }

    pm.lastbomb = Timestamp.from(Instant.now());

    List<PlayerModel> models = Playerrepo.find("game", gm).list();
    gameEntity.players = new ArrayList<>();

    for (PlayerModel l : models) {
        if (Objects.equals(l.id, playerId))
        {
            l.posX = posX;
            l.posY = posY;
            l.lastbomb = Timestamp.from(Instant.now());
        }
        PlayerEntity d = PlayerConverter.to_entity(l);
        d.game.players.add(d);
        gameEntity.players.add(d);
    }

    pm.posY = posY;
    pm.posX = posX;

    //Thread here
    CompletableFuture.runAsync(() -> Explode(gameId, playerId, posX, posY), CompletableFuture.delayedExecutor(delay_bomb_time * duration, TimeUnit.MILLISECONDS));

    return gameEntity;
}

@Transactional
public void Explode(Long gameId, Long playerId, int posX, int posY)
{
    //PlayerModel pm = Playerrepo.findById(playerId);
    GameModel gm = Gamerepo.findById(gameId);

    for (int i = 0; i < gm.game_map.size(); i++)
    {
        gm.game_map.set(i, runLengthDecode(gm.game_map.get(i)));
    }

    //Case Y + 1 may overflow

    if (gm.game_map.get(posY + 1).charAt(posX) == 'W')
    {
        String s = gm.game_map.get(posY + 1).substring(0, posX) + 'G' + gm.game_map.get(posY).substring(posX + 1);
        gm.game_map.set(posY + 1, s);
    }



    //Case Y - 1 may overflow

    if (gm.game_map.get(posY - 1).charAt(posX) == 'W')
    {
        String s = gm.game_map.get(posY - 1).substring(0, posX) + 'G' + gm.game_map.get(posY).substring(posX + 1);
        gm.game_map.set(posY - 1, s);
    }



    //Case X + 1 may overflow

    if (gm.game_map.get(posY).charAt(posX + 1) == 'W')
    {
        String s = gm.game_map.get(posY).substring(0, posX + 1) + 'G' + gm.game_map.get(posY).substring(posX + 2);
        gm.game_map.set(posY, s);
    }



    //Case x - 1 may overflow

    if (gm.game_map.get(posY).charAt(posX - 1)== 'W')
    {
        String s = gm.game_map.get(posY).substring(0, posX - 1) + 'G' + gm.game_map.get(posY).substring(posX);
        gm.game_map.set(posY, s);
    }



    //Case same square



    List<PlayerModel> models = Playerrepo.find("game", gm).list();

    for (PlayerModel pm:  models) {
        if (posY == pm.posY && posX == pm.posX)
        {
            pm.lives--;
        }
        if (posY == pm.posY && posX - 1 == pm.posX)
        {
            pm.lives--;
        }
        if (posY == pm.posY && posX + 1 == pm.posX)
        {
            pm.lives--;
        }
        if (posY - 1 == pm.posY && posX == pm.posX)
        {
            pm.lives--;
        }
        if (posY + 1 == pm.posY && posX == pm.posX)
        {
            pm.lives--;
        }
    }

    int count = 0;
    for (int i = 0; i < models.size(); i++)
    {

       if (models.get(i).lives > 0)
            count++;
    }

    if (count <= 1)
        gm.state = "FINISHED";

    //Map back to normal
    gm.game_map.set(posY, gm.game_map.get(posY).replace('B', 'G'));

    for (int i = 0; i < gm.game_map.size(); i++)
    {
        gm.game_map.set(i, runLengthEncode(gm.game_map.get(i)));
    }
}


    @Transactional
    public GameEntity MovePlayer(Long gameId, Long playerId, int posX, int posY) throws CustomException {
        //Place above
        PlayerModel pm = Playerrepo.findById(playerId);
        if (pm == null || playerId == null)
        {
            throw new NotFoundException();
        }

        if (pm.lives == 0)
            throw new BadRequestException();

        GameModel gm = Gamerepo.findById(gameId);

        if (gm == null || gameId == null)
            throw new NotFoundException();

        if (gm.game_map == null || gm.game_map.size() < 15)
            throw new CustomException("message", 429);

        if (Math.abs(posX - pm.posX) == 1 && Math.abs(posY - pm.posY) == 1)
            throw new BadRequestException();

        if (Math.abs(posX - pm.posX) > 1)
            throw new BadRequestException();

        if (Math.abs(posY - pm.posY) > 1)
            throw new BadRequestException();

        if (!gm.state.equals("RUNNING"))
            throw new BadRequestException();

        GameEntity gameEntity = GameConverter.to_entity(gm);

        for (int i = 0; i < gameEntity.game_map.size(); i++)
        {
            gameEntity.game_map.set(i, runLengthDecode(gameEntity.game_map.get(i)));
        }

        if (gameEntity.game_map.get(posY).charAt(posX) != 'G')
        {
            for (int i = 0; i < gameEntity.game_map.size(); i++)
            {
                gameEntity.game_map.set(i, runLengthEncode(gameEntity.game_map.get(i)));
            }
            throw new BadRequestException();
        }

        for (int i = 0; i < gameEntity.game_map.size(); i++)
        {
            gameEntity.game_map.set(i, runLengthEncode(gameEntity.game_map.get(i)));
        }

        Timestamp t = Timestamp.from(Instant.now());

        if (t.getTime() - pm.lastmovement.getTime() < movement_time * duration)
        {
            throw new CustomException("Custom", 429);
        }

        List<PlayerModel> models = Playerrepo.find("game", gm).list();
        gameEntity.players = new ArrayList<>();

        for (PlayerModel l : models) {
            if (Objects.equals(l.id, playerId))
            {
                l.posX = posX;
                l.posY = posY;
                l.lastmovement = t;
            }
            PlayerEntity d = PlayerConverter.to_entity(l);
            d.game.players.add(d);
            gameEntity.players.add(d);
        }

        pm.posY = posY;
        pm.posX = posX;
        pm.lastmovement = t;

        return gameEntity;
    }

}
