package it.unicam.cs.pawm.chessbackend.model.game;

/**
 * This enum represent the state in which the game pieces could be during a chess game.
 *
 * If a piece gets eaten by another piece, its state get swapped to <code>DEAD</code>.
 * If the state of a piece is <code>DEAD</code> (because it was previously eaten), an is turned
 * back alive after a promotion event occur, is state is swapped to <code>ALIVE</code>.
 */
public enum PieceState {
    ALIVE,
    DEAD;

    public boolean isAlive(){
        return this==ALIVE;
    }

    public boolean isDead(){
        return this==DEAD;
    }

    public PieceState swap(){
        return switch (this){
            case ALIVE -> DEAD;
            case DEAD -> ALIVE;
        };
    }
}
