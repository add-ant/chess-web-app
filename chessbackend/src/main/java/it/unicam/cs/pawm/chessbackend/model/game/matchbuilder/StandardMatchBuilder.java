package it.unicam.cs.pawm.chessbackend.model.game.matchbuilder;

import it.unicam.cs.pawm.chessbackend.model.game.match.Match;
import it.unicam.cs.pawm.chessbackend.model.game.match.PlayerPair;
import it.unicam.cs.pawm.chessbackend.model.game.match.WaitingRoom;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class StandardMatchBuilder implements MatchBuilder{
    private static int lastId = 0;
    @Override
    public Match createMatch(WaitingRoom room) {
        Optional<PlayerPair> players = room.getPlayersForMatch();
        if (players.isPresent()){
            return new Match(lastId++, 60000, players.get());
        } else
            throw new IllegalStateException("Unable to create match: waiting room has not enough players.");
    }
}
