package it.unicam.cs.pawm.chessbackend.model.game.match;

import it.unicam.cs.pawm.chessbackend.model.game.Chessboard;
import it.unicam.cs.pawm.chessbackend.model.game.Evaluator;
import it.unicam.cs.pawm.chessbackend.model.game.Move;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * This class represents a chess match between two players.
 *
 * A chess match can be view as a list of the moves made by the two players.
 *
 * A match can have different endings:
 *      - checkmate: if one player makes a checkmate move, the match ends and that player wins the match.
 *      - Stall: if one player has no legal moves to make but his king is not under check, the match ends
 *               and no one wins.
 *      - End time: if a player ends his time to make moves, the other player wins.
 */
public class Match {
    private final Player whitePlayer;
    private final Player blackPlayer;
    private final Map<Player, ChessTimer> timers;
    private final Map<Player, List<Move>> moves;
    private final Evaluator moveEvaluator;

    public Match(Player whitePlayer, Player blackPlayer, Chessboard board) {
        this.whitePlayer = Objects.requireNonNull(whitePlayer);
        this.blackPlayer = Objects.requireNonNull(blackPlayer);
        this.moveEvaluator = new Evaluator(Objects.requireNonNull(board));
        this.timers = new HashMap<>();
        this.timers.put(whitePlayer, new ChessTimer());
        this.timers.put(blackPlayer, new ChessTimer());
        this.moves = new HashMap<>();
    }

    public Player getWhitePlayer() {
        return whitePlayer;
    }

    public Player getBlackPlayer() {
        return blackPlayer;
    }

    public Map<Player, ChessTimer> getTimers() {
        return timers;
    }

    public Map<Player, List<Move>> getMoves() {
        return moves;
    }

    public Evaluator getMoveEvaluator() {
        return moveEvaluator;
    }
}
