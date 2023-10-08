package it.unicam.cs.pawm.chessbackend;

import it.unicam.cs.pawm.chessbackend.model.game.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ChessboardTest {
    private static Chessboard chessboard;

    @BeforeAll
    static void chessboardConstruction(){
        chessboard = new Chessboard();
    }

    @BeforeEach
    public void reset(){
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
    public void squareForGivenPieceShouldBeCorrect(){
        Piece pawn = new Piece(1, PieceType.PAWN, Color.WHITE);
        chessboard.getSquareAt(5, 5).occupyWith(pawn);

        assertEquals(chessboard.getSquareAt(5, 5), chessboard.getSquareFor(pawn));
    }

    @Test
    public void shouldReturnCorrectListOfEmptySquares(){
        Piece pawn = new Piece(1, PieceType.PAWN, Color.WHITE);
        chessboard.getSquareAt(5, 5).occupyWith(pawn);
        chessboard.getSquareAt(5, 6).occupyWith(pawn);

        assertEquals(4, chessboard.getEmptySquaresFrom(chessboard.getSquareAt(0, 5), Direction.UP).size());
        assertEquals(1, chessboard.getEmptySquaresFrom(chessboard.getSquareAt(7, 6), Direction.DOWN).size());
        assertEquals(7, chessboard.getEmptySquaresFrom(chessboard.getSquareAt(1, 7), Direction.LEFT).size());
        assertEquals(4, chessboard.getEmptySquaresFrom(chessboard.getSquareAt(5, 0), Direction.RIGTH).size());
        assertEquals(4, chessboard.getEmptySquaresFrom(chessboard.getSquareAt(0, 0), Direction.UPRIGTH).size());
        assertEquals(1, chessboard.getEmptySquaresFrom(chessboard.getSquareAt(7, 7), Direction.DOWNLEFT).size());
        assertEquals(1, chessboard.getEmptySquaresFrom(chessboard.getSquareAt(7, 3), Direction.DOWNRIGTH).size());
        assertEquals(1, chessboard.getEmptySquaresFrom(chessboard.getSquareAt(3, 7), Direction.UPLEFT).size());

    }

    @Test
    public void shouldReturnCorrectSquares(){
        Square origin = chessboard.getSquareAt(0,0);

        assertTrue(chessboard.getSquareFrom(origin, Direction.UP, Direction.RIGTH, 2, 1).isPresent());
        assertTrue(chessboard.getSquareFrom(origin, Direction.RIGTH, Direction.UP, 2, 1).isPresent());
        assertFalse(chessboard.getSquareFrom(origin, Direction.UP, Direction.LEFT, 2, 1).isPresent());

        System.out.println(chessboard.getSquareFrom(origin, Direction.UP, Direction.RIGTH, 2, 1));
    }

    @Test
    public void boardShouldBeCorrectlyInitialized(){
        Set<Piece> whitePieces = PieceFactory.getSet(Color.WHITE);
        Set<Piece> blackPieces = PieceFactory.getSet(Color.BLACK);

        chessboard.initializeBoard(whitePieces, blackPieces);

        for (int i = 0; i < 8; i++){
            assertTrue(chessboard.getSquareAt(1, i).getPiece().isPresent());
            assertTrue(chessboard.getSquareAt(6, i).getPiece().isPresent());
            assertEquals(PieceType.PAWN, chessboard.getSquareAt(1, i).getPiece().get().getPieceType());
            assertEquals(PieceType.PAWN, chessboard.getSquareAt(6, i).getPiece().get().getPieceType());
            assertEquals(Color.WHITE, chessboard.getSquareAt(1, i).getPiece().get().getColor());
            assertEquals(Color.BLACK, chessboard.getSquareAt(6, i).getPiece().get().getColor());
        }

        for (int i = 0; i < 8; i++){
            assertTrue(chessboard.getSquareAt(0, i).getPiece().isPresent());
            assertTrue(chessboard.getSquareAt(7, i).getPiece().isPresent());
            assertEquals(Color.WHITE, chessboard.getSquareAt(0, i).getPiece().get().getColor());
            assertEquals(Color.BLACK, chessboard.getSquareAt(7, i).getPiece().get().getColor());
        }
    }
}
