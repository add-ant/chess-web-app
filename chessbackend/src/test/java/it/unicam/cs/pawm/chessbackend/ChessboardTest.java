package it.unicam.cs.pawm.chessbackend;

import it.unicam.cs.pawm.chessbackend.model.game.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    public void shouldEvolveCorrectly(){
        Piece pawn = new Piece(1, PieceType.PAWN, Color.WHITE);
        Square origin = chessboard.getSquareAt(0, 1);
        Square target = chessboard.getSquareAt(0, 2);
        origin.occupyWith(pawn);

        assertFalse(chessboard.getSquareAt(0, 1).isEmpty());

        Move move = new Move(origin, target, pawn, MoveEffect.MOVE);
        chessboard.evolve(move);

        assertTrue(chessboard.getSquareAt(0, 1).isEmpty());
        assertFalse(chessboard.getSquareAt(0, 2).isEmpty());
    }
}
