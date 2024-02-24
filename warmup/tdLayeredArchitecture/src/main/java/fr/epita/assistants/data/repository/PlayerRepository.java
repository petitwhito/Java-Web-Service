package fr.epita.assistants.data.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fr.epita.assistants.Piece;
import fr.epita.assistants.Player;
import fr.epita.assistants.PlayerService;

import java.io.File;
import java.nio.file.Files;

public class PlayerRepository extends Repository {
    public static void load(PlayerService playerService) {
        try {
            if (!Files.exists(PATH)) {
                Files.createFile(PATH);
            }
            JsonNode jsonNode = objectMapper.readTree(PATH.toFile());
            ArrayNode players = (ArrayNode) jsonNode.get("players");
            if (players == null) {
                return;
            }
            players.forEach(player -> {
                try {
                    playerService.addPlayer(objectMapper.readValue(player.asText(), Player.class));
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (Exception e) {
            throw new RuntimeException("Cannot load from corrupted files", e);
        }
    }

    public static void save(Player player) {
        try {
            ObjectNode objectNode = setupSave();

            ArrayNode jsonPlayer = (ArrayNode) objectNode.get("players");
            if (jsonPlayer == null) {
                objectNode.set("players", objectMapper.createArrayNode());
                jsonPlayer = (ArrayNode) objectNode.get("players");
            }
            jsonPlayer.add(objectMapper.writeValueAsString(player));
            objectMapper.writeValue(PATH.toFile(), objectNode);
            for (Piece piece : player.pieces) {
                PieceRepository.save(piece);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to save player: " + e);
        }
    }

}
