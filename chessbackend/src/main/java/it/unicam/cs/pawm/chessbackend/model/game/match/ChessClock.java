package it.unicam.cs.pawm.chessbackend.model.game.match;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Represents a clock used in a chess match: the chess clock keeps track of each player remaining time.
 */
public class ChessClock {
    private final Map<Player, SimpleTimer> playerTimers;

    private Player previousTurnPlayer;

    public ChessClock(PlayerPair players, long timeAmount) {
        playerTimers = new HashMap<>();
        setClock(players, timeAmount);
    }

    private void setClock(PlayerPair players, long timeAmount) {
        playerTimers.put(players.getWhitePlayer(), new SimpleTimer(timeAmount));
        playerTimers.put(players.getBlackPlayer(), new SimpleTimer(timeAmount));
    }

    public long getRemainingTime(Player player){
        return playerTimers.get(player).getTimeLeft();
    }

    public Optional<Player> getPlayerWithoutTime(){
        return playerTimers.keySet().stream().filter(p -> getRemainingTime(p) == 0).findAny();
    }

    public Player getPreviousTurnPlayer() {
        return previousTurnPlayer;
    }

    public Map<Player, SimpleTimer> getPlayerTimers() {
        return playerTimers;
    }


    /**
     * Returns true if at least one player finished is time to play: if this happens, the timer is
     * considered stopped and cannot be started again for either of the two players.
     *
     * @return true if at least one player finished is time to play, false otherwise.
     */
    public boolean isStopped(){
        return playerTimers.entrySet().stream()
            .filter(e -> e.getValue().getTimeLeft() == 0)
                .count() == 2;
    }

    public void startFor(Player player){
        if (!isStopped())
            playerTimers.get(player).start();
    }

    public void pauseFor(Player player){
        if (getCurrentlyPlaying().equals(player))
            playerTimers.get(player).pause();
    }

    public Player getCurrentlyPlaying(){
        return playerTimers.entrySet().stream()
            .filter(e -> !e.getValue().isPaused())
                .map(Map.Entry::getKey)
                    .findFirst()
                        .orElseThrow();
    }

    public Player getCurrentlyWaiting(){
        return playerTimers.keySet().stream()
            .filter(p -> !p.equals(getCurrentlyPlaying()))
                .findFirst()
                    .orElseThrow();
    }

    public void switchPlayer(){
        previousTurnPlayer = getCurrentlyPlaying();
        pauseFor(getCurrentlyPlaying());
        startFor(getOther(previousTurnPlayer));
    }

    private Player getOther(Player player) {
        return playerTimers.keySet().stream().filter(p -> !p.equals(player)).findFirst().orElseThrow();
    }

    public void stop(){
        previousTurnPlayer = getCurrentlyPlaying();
        playerTimers.forEach((player, timer) -> timer.stop());
    }
}
