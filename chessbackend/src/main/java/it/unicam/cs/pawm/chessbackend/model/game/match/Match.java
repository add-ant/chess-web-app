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
    private final int id;
    private final PlayerPair players;
    private MoveEffect lastMoveEffect;
    private final ChessClock clock;
    private final MatchHistory history;
    private final Evaluator moveEvaluator;

    public Match(int id, long timerAmount, PlayerPair players) {
        this.id = id;
        this.players = players;
        this.moveEvaluator = new Evaluator(new Chessboard());
        this.moveEvaluator.getCalculator().getChessboard().initializeBoard(
            players.getWhitePlayer().getPieces(),
            players.getBlackPlayer().getPieces()
        );
        this.clock = new ChessClock(players, timerAmount);
        this.history = new MatchHistory(players);
    }

    public int getId() {
        return id;
    }

    public MoveEffect getLastMoveEffect() {
        return lastMoveEffect;
    }

    public Player getWhitePlayer() {
        return players.getWhitePlayer();
    }

    public Player getBlackPlayer() {
        return players.getBlackPlayer();
    }

    public PlayerPair getPlayers() {
        return players;
    }

    public ChessClock getClock() {
        return clock;
    }

    public MatchHistory getHistory() {
        return history;
    }

    public Evaluator getMoveEvaluator() {
        return moveEvaluator;
    }

    public void start(){
        clock.startFor(players.getWhitePlayer());
    }

    public void switchTurn(){
        clock.switchPlayer();
    }

    public void onMove(Move move){
        if (isEnded())
            throw new IllegalStateException("Match ended: cannot perform any more moves");

        if (!move.getPiece().getColor().equals(clock.getCurrentlyPlaying().getTeam()))
            throw new IllegalArgumentException("Cannot move other player pieces");

        lastMoveEffect = moveEvaluator.evaluate(move);

        if (!lastMoveEffect.equals(MoveEffect.ILLEGAL)){
            history.add(move);
            if (lastMoveEffect.equals(MoveEffect.CHECKMATE) || lastMoveEffect.equals(MoveEffect.DRAW)){
                clock.stop();
            } else {
                switchTurn();
            }
        }
    }

    public boolean isEnded(){
        return getEnding().isPresent();
    }

    public Optional<MatchEndingResult> getEnding() {
        if (!clock.isStopped() && clock.getPlayerWithoutTime().isPresent())
            return Optional.of(MatchEndingResult.TIME);
        if (!(lastMoveEffect == null)){
            if (lastMoveEffect.equals(MoveEffect.CHECKMATE))
                return Optional.of(MatchEndingResult.CHECKMATE_END);
            if (lastMoveEffect.equals(MoveEffect.DRAW))
                return Optional.of(MatchEndingResult.DRAW);
        }
        return Optional.empty();
    }

    public Optional<Player> getWinner(){
        if (getEnding().isPresent()){
            if (getEnding().get().equals(MatchEndingResult.TIME))
                return clock.getPlayerTimers().keySet().stream()
                        .filter(p -> !p.equals(clock.getPlayerWithoutTime().orElseThrow()))
                            .findFirst();
            else if (getEnding().get().equals(MatchEndingResult.CHECKMATE_END))
                return Optional.of(clock.getPreviousTurnPlayer());
        }
        return Optional.empty();
    }
}
