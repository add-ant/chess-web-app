package it.unicam.cs.pawm.chessbackend.model.game.match;

import it.unicam.cs.pawm.chessbackend.model.game.Color;
import it.unicam.cs.pawm.chessbackend.model.game.Piece;
import it.unicam.cs.pawm.chessbackend.model.game.PieceFactory;

import java.util.Objects;
import java.util.Set;

public class Player {
    private final Color team;
    private final String nickname;
    private final int elo;
    private final Set<Piece> pieces;

    public Player(Color team, String nickname, int elo) {
        this.team = team;
        this.nickname = nickname;
        this.elo = elo;
        pieces = PieceFactory.getSet(team);
    }

    public Color getTeam() {
        return team;
    }

    public String getNickname() {
        return nickname;
    }

    public int getElo() {
        return elo;
    }

    public Set<Piece> getPieces() {
        return pieces;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return elo == player.elo && team == player.team && nickname.equals(player.nickname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(team, nickname, elo);
    }
}
