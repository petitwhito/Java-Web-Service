package fr.epita.assistants;

import java.util.ArrayList;
import java.util.List;

public class PieceService {
    List<Piece> pieces;

    public PieceService() {
        this.pieces = new ArrayList<>();
    }

    public void addPiece(Piece piece) {
        this.pieces.add(piece);
    }
}
