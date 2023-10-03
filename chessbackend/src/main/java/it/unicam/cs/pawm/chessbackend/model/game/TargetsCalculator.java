package it.unicam.cs.pawm.chessbackend.model.game;

import java.util.HashSet;
import java.util.Set;

public class TargetsCalculator {
    private final Chessboard chessboard;

    public TargetsCalculator(Chessboard chessboard) {
        this.chessboard = chessboard;
    }

    public Set<Square> computePossibleTargets(Piece piece){
        Square origin = chessboard.getSquareFor(piece);
        return switch (piece.getPieceType()){
            case PAWN -> null;
            case ROOK -> null;
            case BISHOP -> null;
            case KNIGTH -> null;
            case QUEEN -> null;
            case KING -> null;
        };
    }


}
