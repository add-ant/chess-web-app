package it.unicam.cs.pawm.chessbackend.service.beans;

import it.unicam.cs.pawm.chessbackend.model.game.Chessboard;
import it.unicam.cs.pawm.chessbackend.model.game.Color;
import it.unicam.cs.pawm.chessbackend.model.game.match.Player;
import it.unicam.cs.pawm.chessbackend.model.game.match.WaitingRoom;
import it.unicam.cs.pawm.chessbackend.model.game.matchbuilder.MatchBuilder;
import it.unicam.cs.pawm.chessbackend.model.game.matchbuilder.StandardMatchBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class Config {

    @Bean
    public Chessboard chessboard(){
        return new Chessboard();
    }

    @Bean
    public MatchBuilder builder(){
        return new StandardMatchBuilder();
    }

    @Bean
    public WaitingRoom room(){
        Player white = new Player(Color.WHITE, "anto", 600);
        Player black = new Player(Color.BLACK, "ben", 600);
        WaitingRoom room = new WaitingRoom();
        room.addPlayer(white);
        room.addPlayer(black);
        return room;
    }

}
