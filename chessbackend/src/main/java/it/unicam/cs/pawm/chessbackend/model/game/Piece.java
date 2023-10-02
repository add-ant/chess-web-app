package it.unicam.cs.pawm.chessbackend.model.game;

import java.util.Objects;

/**
 * This abstract class describes a generic chess piece.
 *
 * A chess piece has a color and a state that tells if the piece is alive or dead.
 */
public abstract class Piece {
    private final int id;
    private final String name;
    private final Color color;
    private PieceState state;

    protected Piece(int id, String name, Color color) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.state = PieceState.ALIVE;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
