package fr.epita.assistants.data.repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Repository {
    protected static Path PATH = Path.of("./db.yaka2025");
    protected final static ObjectMapper objectMapper = new ObjectMapper();

    static ObjectNode setupSave() throws IOException {
        if (!Files.exists(PATH)) {
            Files.createFile(PATH);
        }
        ObjectNode objectNode;
        JsonNode jsonNode = objectMapper.readTree(PATH.toFile());
        try {
            objectNode = (ObjectNode) jsonNode;
        } catch (Exception ignored) {
            objectNode = objectMapper.createObjectNode();
            objectNode.set("players", objectMapper.createArrayNode());
            objectNode.set("pieces", objectMapper.createArrayNode());
        }
        return objectNode;
    }

}
