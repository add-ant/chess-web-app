package it.unicam.cs.pawm.chessbackend.model.game;

import java.util.Objects;

/**
 * This abstract class describes a generic chess piece.
 *
 * A chess piece has a color and a state that tells if the piece is alive or dead.
 */
public class Piece {
    private final int id;
    private final PieceType pieceType;
    private final Color color;
    private PieceState state;
    private boolean moved;

    public Piece(int id, PieceType pieceType, Color color) {
        this.id = id;
        this.pieceType = pieceType;
        this.color = color;
        this.state = PieceState.ALIVE;
    }

    public void setState(PieceState state) {
        this.state = state;
    }

    public boolean hasMoved() {
        return moved;
    }

    public void setMoved(){
        moved = true;
    }

    public int getId() {
        return id;
    }

    public PieceType getPieceType() {
        return pieceType;
    }

    public Color getColor() {
        return color;
    }

    public PieceState getState() {
        return state;
    }

    public void turnDead(){
        if (state.isAlive())
            state = state.swap();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Piece piece = (Piece) o;
        return id == piece.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
