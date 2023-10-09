package it.unicam.cs.pawm.chessbackend.model.game.match;

import it.unicam.cs.pawm.chessbackend.model.game.Color;
import it.unicam.cs.pawm.chessbackend.model.game.Move;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represent the sequence of all the moves made by the players during a chess match.
 */
public class MatchHistory {
    private final Map<Player, List<Move>> history = new HashMap<>();

    public MatchHistory(PlayerPair players) {
        history.put(players.getWhitePlayer(), new ArrayList<>());
        history.put(players.getBlackPlayer(), new ArrayList<>());
    }

    public Map<Player, List<Move>> getHistory() {
        return history;
    }

    public List<Move> getHistoryOf(Player player){
        return history.get(player);
    }

    public void add(Move move){
        if (move.getPiece().getColor().equals(Color.WHITE)){
            addForTeam(move, Color.WHITE);
        } else
            addForTeam(move, Color.BLACK);
    }

    private void addForTeam(Move move, Color color) {
        history.entrySet().stream()
            .filter(e -> e.getKey().getTeam().equals(color))
            .forEach(e -> e.getValue().add(move));
    }

    public void reset(){
        history.clear();
    }
}
