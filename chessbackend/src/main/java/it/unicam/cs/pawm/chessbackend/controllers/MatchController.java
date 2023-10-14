package it.unicam.cs.pawm.chessbackend.controllers;

import it.unicam.cs.pawm.chessbackend.model.game.Chessboard;
import it.unicam.cs.pawm.chessbackend.model.game.Move;
import it.unicam.cs.pawm.chessbackend.model.game.Square;
import it.unicam.cs.pawm.chessbackend.model.game.match.Match;
import it.unicam.cs.pawm.chessbackend.model.game.match.WaitingRoom;
import it.unicam.cs.pawm.chessbackend.model.game.matchbuilder.MatchBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class MatchController {
    private final Match match;

    public MatchController(MatchBuilder builder, WaitingRoom room) {
        this.match = builder.createMatch(room);
        match.start();
    }

    @GetMapping(value = "/match")
    public Square[][] getCurrentBoard(){
        return match.getMoveEvaluator().getCalculator().getChessboard().getChessboard();
    }

    @GetMapping(path = "/match/moves")
    public String getMoveEffect(@RequestParam(name = "ox") int originX, @RequestParam(name = "oy") int originY,
                                        @RequestParam(name = "tx") int targetX, @RequestParam(name = "ty") int targetY){
        Chessboard board = match.getMoveEvaluator().getCalculator().getChessboard();
        Move moveToMake = new Move(
            board.getSquareAt(originX, originY),
            board.getSquareAt(targetX, targetY)
        );
        match.onMove(moveToMake);
        return "{\"type\" : \"" + match.getLastMoveEffect().name() + "\"}";
    }


}
