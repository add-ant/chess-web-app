package it.unicam.cs.pawm.chessbackend.model.game.match;

import it.unicam.cs.pawm.chessbackend.model.game.*;

import java.util.*;

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
    private MatchEndingResult result = null;
    private Player winner = null;
    private final Chessboard board;
    private final Map<Player, ChessTimer> timers;
    private final Map<Player, List<Move>> moves;
    private final Evaluator moveEvaluator;
    private Player currentTurnPlayer;


    public Match(Player whitePlayer, Player blackPlayer, Chessboard board, long timerAmount) {
        this.whitePlayer = Objects.requireNonNull(whitePlayer);
        this.blackPlayer = Objects.requireNonNull(blackPlayer);
        this.board = Objects.requireNonNull(board);
        this.board.initializeBoard(whitePlayer.getPieces(), blackPlayer.getPieces());
        this.currentTurnPlayer = whitePlayer;
        this.moveEvaluator = new Evaluator(this.board);
        this.timers = new HashMap<>();
        this.timers.put(whitePlayer, new ChessTimer(timerAmount));
        this.timers.put(blackPlayer, new ChessTimer(timerAmount));
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

    public void start(){
        timers.get(currentTurnPlayer).start();
    }

    public void switchTurn(){
        timers.get(currentTurnPlayer).pause();
        if (currentTurnPlayer.getTeam().equals(Color.WHITE)){
            currentTurnPlayer = blackPlayer;
            timers.get(currentTurnPlayer).start();
        } else {
            currentTurnPlayer = whitePlayer;
            timers.get(currentTurnPlayer).start();
        }
    }

    public void handleMove(Move move){
        if (!move.getPiece().getColor().equals(currentTurnPlayer.getTeam()))
            throw new IllegalArgumentException("Cannot move other player pieces");
        MoveEffect effect = moveEvaluator.evaluate(move);
        if (!effect.equals(MoveEffect.ILLEGAL)){
            if (effect.equals(MoveEffect.CHECKMATE)){
                winner = currentTurnPlayer;
                result = MatchEndingResult.CHECKMATE_END;
            } else if (effect.equals(MoveEffect.DRAW)){
                result = MatchEndingResult.DRAW;
            } else {
                switchTurn();
            }
        }
    }

    public Optional<MatchEndingResult> getEnding() {
        if (result == null)
            return Optional.empty();
        return Optional.of(result);
    }

    public Optional<Player> getWinner(){
        if (winner == null)
            return Optional.empty();
        return Optional.of(winner);
    }
}
