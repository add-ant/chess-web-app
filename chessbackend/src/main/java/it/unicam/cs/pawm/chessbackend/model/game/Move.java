package it.unicam.cs.pawm.chessbackend.model.game;

import java.util.Objects;

/**
 * This class represents a move in the chess game.
 *
 * A move is a shift of a game piece from the squares that is currently occupying and the target
 * square that it wants to reach.
 */
public class Move {
    private final Square origin;
    private final Square target;
    private final Piece piece;
    private final MoveEffect effect;

    public Move(Square origin, Square target, Piece piece, MoveEffect effect) {
        this.origin = origin;
        this.target = target;
        this.piece = piece;
        this.effect = effect;
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

    public MoveEffect getEffect() {
        return effect;
    }

    /**
     * Transfers the moving piece to the target square, if the move is not illegal.
     */
    public void perform(){
        if(!effect.equals(MoveEffect.ILLEGAL)){
            target.occupyWith(piece);
            origin.free();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return Objects.equals(origin, move.origin) && Objects.equals(target, move.target) && Objects.equals(piece, move.piece) && effect == move.effect;
    }

    @Override
    public int hashCode() {
        return Objects.hash(origin, target, piece, effect);
    }
}
