package fr.epita.assistants.data.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fr.epita.assistants.Piece;
import fr.epita.assistants.PieceService;

import java.io.File;
import java.nio.file.Files;

public class PieceRepository extends Repository {

    public static void save(Piece piece) {
        try {
            ObjectNode objectNode = setupSave();

            ArrayNode jsonUnit = (ArrayNode) objectNode.get("pieces");
            if (jsonUnit == null) {
                objectNode.set("pieces", objectMapper.createArrayNode());
                jsonUnit = (ArrayNode) objectNode.get("pieces");
            }
            jsonUnit.add(objectMapper.writeValueAsString(piece));

            objectMapper.writeValue(PATH.toFile(), objectNode);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save player: " + e);
        }
    }

    public static void load(PieceService pieceService) {
        try {
            if (!Files.exists(PATH)) {
                Files.createFile(PATH);
            }
            JsonNode jsonNode = objectMapper.readTree(PATH.toFile());
            ArrayNode pieces = (ArrayNode) jsonNode.get("pieces");
            if (pieces == null) {
                return;
            }
            pieces.forEach(unit -> {
                try {
                    pieceService.addPiece(objectMapper.readValue(unit.asText(), Piece.class));
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (Exception e) {
            throw new RuntimeException("Cannot load from corrupted files", e);
        }
    }
}
