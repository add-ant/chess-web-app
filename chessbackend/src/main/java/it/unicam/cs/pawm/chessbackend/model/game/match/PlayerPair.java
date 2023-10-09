package it.unicam.cs.pawm.chessbackend.model.game.match;

import it.unicam.cs.pawm.chessbackend.model.game.Color;

/**
 * Represents a pair of player in a chess match.
 * The players in a pair must belong to different teams.
 */
public class PlayerPair {
    private final Player whitePlayer;
    private final Player blackPlayer;

    public PlayerPair(Player whitePlayer, Player blackPlayer) {
        if (!whitePlayer.getTeam().equals(Color.WHITE))
            throw new IllegalArgumentException("White player is not white.");
        if (!blackPlayer.getTeam().equals(Color.BLACK))
            throw new IllegalArgumentException("Black player is not black.");
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
    }

    public Player getWhitePlayer() {
        return whitePlayer;
    }

    public Player getBlackPlayer() {
        return blackPlayer;
    }
}
