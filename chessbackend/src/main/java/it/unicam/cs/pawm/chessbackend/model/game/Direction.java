package it.unicam.cs.pawm.chessbackend.model.game;

public enum Direction {
    UP, DOWN, LEFT, RIGTH, UPRIGTH, UPLEFT, DOWNRIGTH, DOWNLEFT;

    boolean isDiagonal(){
        return this==UPLEFT || this==UPRIGTH || this==DOWNLEFT || this==DOWNRIGTH;
    }
}
