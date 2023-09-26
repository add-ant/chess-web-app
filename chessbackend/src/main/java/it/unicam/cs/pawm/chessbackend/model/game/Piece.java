package it.unicam.cs.pawm.chessbackend.model.game;

/**
 * This abstract class describes a generic chess piece.
 *
 * A chess piece has a color and a state that tells if the piece is alive or dead.
 */
public abstract class Piece {
    private final Color color;
    private PieceState state;

    protected Piece(Color color) {
        this.color = color;
        this.state = PieceState.ALIVE;
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
}
