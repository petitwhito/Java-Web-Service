package fr.epita.assistants;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@ToString()
@Getter
public class Piece {
    private static int count = 1;
    private final int id;
    private int posX;
    private int posY;
    public Piece(int posX, int posY) {
        this.id = count++;
        this.posX = posX;
        this.posY = posY;
    }

    public Piece(@JsonProperty("id") int id, @JsonProperty("posX") int posX, @JsonProperty("posY") int posY) {
        this.id = id;
        this.posX = posX;
        this.posY = posY;
        count = id + 1;
    }

    public void moveUpRight() {
        posY++;
        posX++;
    }
    public void moveUpLeft() {
        posY++;
        posX--;
    }
    public void moveDownRight() {
        posX++;
        posY--;
    }
    public void moveDownLeft() {
        posX--;
        posY--;
    }
}
