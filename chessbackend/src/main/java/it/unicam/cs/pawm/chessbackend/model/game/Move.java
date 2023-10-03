package it.unicam.cs.pawm.chessbackend.model.game;

import java.util.Objects;

/**
 * This class represents a move in the chess game.
 *
 * A move is a shift of a game piece from the square that it is currently occupying and the target
 * square that it wants to reach.
 */
public class Move {
    private final Square origin;
    private final Square target;
    private final Piece piece;

    public Move(Square origin, Square target) {
        this.origin = origin;
        this.target = target;
        this.piece = origin.getPiece().orElseThrow();
    }

    public Square getOrigin() {
        return origin;
    }

    public Square getTarget() {
        return target;
    }

    public Piece getPiece() {
        return piece;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return Objects.equals(origin, move.origin) && Objects.equals(target, move.target) && Objects.equals(piece, move.piece);
    }

    @Override
    public int hashCode() {
        return Objects.hash(origin, target, piece);
    }
}
