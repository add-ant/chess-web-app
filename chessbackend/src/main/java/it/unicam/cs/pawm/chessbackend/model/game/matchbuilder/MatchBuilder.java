package it.unicam.cs.pawm.chessbackend.model.game.matchbuilder;

import it.unicam.cs.pawm.chessbackend.model.game.match.Match;
import it.unicam.cs.pawm.chessbackend.model.game.match.PlayerPair;
import it.unicam.cs.pawm.chessbackend.model.game.match.WaitingRoom;

/**
 * A match builder is responsible for creating a chess match between two players.
 */
public interface MatchBuilder {
    /**
     * Creates a match between two players with a specific time.
     *
     * @param room the waiting room.
     * @return the new Match created.
     */
    Match createMatch(WaitingRoom room);
}
