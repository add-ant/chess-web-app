package it.unicam.cs.pawm.chessbackend.model.game;

public enum Color {
    WHITE,
    BLACK;

    Color swap(){
        return switch (this){
            case WHITE -> BLACK;
            case BLACK -> WHITE;
        };
    }
}
