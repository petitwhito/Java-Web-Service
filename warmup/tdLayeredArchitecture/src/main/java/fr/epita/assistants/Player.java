package fr.epita.assistants;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString()
@Getter
public class Player {
    private static int id = 0;
    public String name;
    @JsonIgnore
    public List<Piece> pieces;

    public Player(@JsonProperty("name") String name) {
        id++;
        this.name = name;
        this.pieces = new ArrayList<>();
    }
}
