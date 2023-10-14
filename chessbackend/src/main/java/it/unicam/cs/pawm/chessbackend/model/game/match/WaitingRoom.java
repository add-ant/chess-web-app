package it.unicam.cs.pawm.chessbackend.model.game.match;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WaitingRoom {
    private final List<Player> waitingPlayers;

    public WaitingRoom() {
        this.waitingPlayers = new ArrayList<>();
    }

    public List<Player> getWaitingPlayers() {
        return waitingPlayers;
    }

    public void addPlayer(Player player){
        if(!waitingPlayers.contains(player))
            waitingPlayers.add(player);
    }

    public Optional<PlayerPair> getPlayersForMatch(){
        Optional<PlayerPair> players = Optional.empty();
        if (waitingPlayers.size() >= 2){
            players = Optional.of(new PlayerPair(waitingPlayers.get(0), waitingPlayers.get(1)));
            waitingPlayers.remove(players.get().getWhitePlayer());
            waitingPlayers.remove(players.get().getBlackPlayer());
        }
        return players;
    }
}
