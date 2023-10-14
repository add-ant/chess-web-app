package it.unicam.cs.pawm.chessbackend.controllers;

import it.unicam.cs.pawm.chessbackend.model.game.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class BoardController {
    private final Chessboard board;

    @Autowired
    public BoardController(Chessboard board) {
        this.board = board;
    }

    @GetMapping
    public Square[][] getConfiguration(){
        board.initializeBoard(PieceFactory.getSet(Color.WHITE), PieceFactory.getSet(Color.BLACK));
        return board.getChessboard();
    }
}
