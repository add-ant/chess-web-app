package it.unicam.cs.pawm.chessbackend;

import it.unicam.cs.pawm.chessbackend.model.game.Chessboard;
import it.unicam.cs.pawm.chessbackend.model.game.Color;
import it.unicam.cs.pawm.chessbackend.model.game.Square;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ChessboardTest {
    private static Chessboard chessboard;

    @BeforeAll
    static void chessboardConstruction(){
        chessboard = new Chessboard();
    }

    @Test
    public void chessboardSquaresShouldBeEmpty(){
        Square[][] boardConfiguration = chessboard.getChessboard();

        for (int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                assertTrue(boardConfiguration[i][j].isEmpty());
            }
        }
    }

    @Test
    public void cornerSquaresShouldBeOfCorrectColour(){
        Square[][] boardConfiguration = chessboard.getChessboard();

        assertEquals(Color.BLACK, boardConfiguration[0][0].getColor());
        assertEquals(Color.WHITE, boardConfiguration[0][7].getColor());
        assertEquals(Color.BLACK, boardConfiguration[7][7].getColor());
        assertEquals(Color.WHITE, boardConfiguration[7][0].getColor());
    }
}
