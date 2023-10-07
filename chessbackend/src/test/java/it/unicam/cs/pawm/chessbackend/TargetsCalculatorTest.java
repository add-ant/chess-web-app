package it.unicam.cs.pawm.chessbackend;

import it.unicam.cs.pawm.chessbackend.model.game.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TargetsCalculatorTest {
    private static Chessboard chessboard;
    private static TargetsCalculator calculator;

    @BeforeAll
    public static void initialize(){
        chessboard = new Chessboard();
        calculator = new TargetsCalculator(chessboard);
    }

    @AfterEach
    public void reset(){
        chessboard = new Chessboard();
        calculator = new TargetsCalculator(chessboard);
    }

    @Test
    public void pawnShouldHaveCorrectVerticalTargets(){
        Piece whitePawn = new Piece(1, PieceType.PAWN, Color.WHITE);
        Piece blackPawn = new Piece(2, PieceType.PAWN, Color.BLACK);
        Piece blockPawn = new Piece(3, PieceType.PAWN, Color.BLACK);

        chessboard.getSquareAt(1, 1).occupyWith(whitePawn);
        chessboard.getSquareAt(6, 1).occupyWith(blackPawn);

        assertEquals(2, calculator.computePossibleTargets(whitePawn).size());
        assertEquals(2, calculator.computePossibleTargets(blackPawn).size());

        chessboard.getSquareAt(3, 1).occupyWith(blockPawn);
        chessboard.getSquareAt(4, 1).occupyWith(blockPawn);

        assertEquals(1, calculator.computePossibleTargets(whitePawn).size());
        assertEquals(1, calculator.computePossibleTargets(blackPawn).size());

        chessboard.getSquareAt(2, 1).occupyWith(blockPawn);
        chessboard.getSquareAt(5, 1).occupyWith(blockPawn);

        assertEquals(0, calculator.computePossibleTargets(whitePawn).size());
        assertEquals(0, calculator.computePossibleTargets(blackPawn).size());

        chessboard.getSquareAt(3, 1).free();
        chessboard.getSquareAt(4, 1).free();
        chessboard.getSquareAt(2, 1).free();
        chessboard.getSquareAt(5, 1).free();

        whitePawn.setMoved();
        blackPawn.setMoved();

        assertEquals(1, calculator.computePossibleTargets(whitePawn).size());
        assertEquals(1, calculator.computePossibleTargets(blackPawn).size());

    }

    @Test
    public void pawnShouldHaveCorrectDiagonalTargets(){
        Piece whitePawn = new Piece(1, PieceType.PAWN, Color.WHITE);
        Piece blackPawn = new Piece(2, PieceType.PAWN, Color.BLACK);
        Piece captured = new Piece(4, PieceType.PAWN, Color.BLACK);
        Piece captured2 = new Piece(4, PieceType.PAWN, Color.WHITE);


        chessboard.getSquareAt(3, 3).occupyWith(whitePawn);
        chessboard.getSquareAt(4, 3).occupyWith(blackPawn);
        chessboard.getSquareAt(4, 4).occupyWith(captured);
        chessboard.getSquareAt(4, 2).occupyWith(captured);
        chessboard.getSquareAt(3, 2).occupyWith(captured2);
        chessboard.getSquareAt(3, 4).occupyWith(captured2);

        assertEquals(2, calculator.computePossibleTargets(whitePawn).size());
        assertEquals(2, calculator.computePossibleTargets(blackPawn).size());

        chessboard.getSquareAt(4, 4).free();
        chessboard.getSquareAt(4, 2).free();
        chessboard.getSquareAt(3, 2).free();
        chessboard.getSquareAt(3, 4).free();

        assertEquals(0, calculator.computePossibleTargets(whitePawn).size());
        assertEquals(0, calculator.computePossibleTargets(blackPawn).size());
    }

    @Test
    public void rookShouldHaveCorrectNumberOfTargets(){
        Piece rook = new Piece(1, PieceType.ROOK, Color.WHITE);
        Piece rook2 = new Piece(2, PieceType.ROOK, Color.BLACK);
        Piece rook3 = new Piece(3, PieceType.ROOK, Color.WHITE);
        Piece rook4 = new Piece(4, PieceType.ROOK, Color.BLACK);
        Piece rook5 = new Piece(5, PieceType.ROOK, Color.WHITE);
        Piece pawn = new Piece(6, PieceType.PAWN, Color.WHITE);


        chessboard.getSquareAt(0, 0).occupyWith(rook);
        chessboard.getSquareAt(7, 0).occupyWith(rook2);
        chessboard.getSquareAt(7, 7).occupyWith(rook3);
        chessboard.getSquareAt(0, 7).occupyWith(rook4);
        chessboard.getSquareAt(2, 2).occupyWith(rook5);
        chessboard.getSquareAt(3, 2).occupyWith(pawn);

        assertEquals(14, calculator.computePossibleTargets(rook).size());
        assertEquals(14, calculator.computePossibleTargets(rook2).size());
        assertEquals(14, calculator.computePossibleTargets(rook3).size());
        assertEquals(14, calculator.computePossibleTargets(rook4).size());
        assertEquals(9, calculator.computePossibleTargets(rook5).size());
    }

    @Test
    public void bishopShouldHaveCorrectNumberOfTargets(){
        Piece bishop = new Piece(1, PieceType.BISHOP, Color.WHITE);
        Piece blackBishop = new Piece(3, PieceType.BISHOP, Color.BLACK);
        Piece rook = new Piece(2, PieceType.ROOK, Color.WHITE);

        chessboard.getSquareAt(0,0).occupyWith(bishop);
        chessboard.getSquareAt(0, 3).occupyWith(blackBishop);

        assertEquals(7, calculator.computePossibleTargets(bishop).size());
        assertEquals(7, calculator.computePossibleTargets(blackBishop).size());

        chessboard.getSquareAt(7, 7).occupyWith(rook);

        assertEquals(6, calculator.computePossibleTargets(bishop).size());
    }

    @Test
    public void queenShouldHaveCorrectNumberOfTargets(){
        Piece queen = new Piece(1, PieceType.QUEEN, Color.WHITE);
        Piece whitePawn = new Piece(2, PieceType.PAWN, Color.WHITE);
        Piece blackRook = new Piece(3, PieceType.ROOK, Color.BLACK);

        chessboard.getSquareAt(0, 0).occupyWith(queen);

        assertEquals(21, calculator.computePossibleTargets(queen).size());

        chessboard.getSquareAt(1, 1).occupyWith(whitePawn);
        chessboard.getSquareAt(0, 4).occupyWith(blackRook);

        assertEquals(11, calculator.computePossibleTargets(queen).size());
    }

    @Test
    public void knightShouldHaveCorrectNumberOfTargets(){
        Piece whiteKnight = new Piece(1, PieceType.KNIGHT, Color.WHITE);
        Piece blackKnight = new Piece(2, PieceType.KNIGHT, Color.BLACK);
        Piece blackRook = new Piece(3, PieceType.ROOK, Color.BLACK);

        chessboard.getSquareAt(0, 0).occupyWith(whiteKnight);
        chessboard.getSquareAt(3, 3).occupyWith(blackKnight);

        assertEquals(2, calculator.computePossibleTargets(whiteKnight).size());
        assertEquals(8, calculator.computePossibleTargets(blackKnight).size());

        chessboard.getSquareAt(4, 1).occupyWith(blackRook);

        assertEquals(7, calculator.computePossibleTargets(blackKnight).size());
    }

    @Test
    public void kingShouldHaveCorrectNumberOfTargets(){
        Piece king = new Piece(1, PieceType.KING, Color.WHITE);
        Piece bishop = new Piece(2, PieceType.BISHOP, Color.WHITE);
        Piece queen = new Piece(3, PieceType.QUEEN, Color.BLACK);
        Piece rook = new Piece(4, PieceType.ROOK, Color.BLACK);
        Piece pawn = new Piece(5, PieceType.PAWN, Color.BLACK);

        chessboard.getSquareAt(0, 4).occupyWith(king);
        chessboard.getSquareAt(0, 5).occupyWith(bishop);

        assertEquals(4, calculator.computePossibleTargets(king).size());

        chessboard.getSquareAt(0, 5).free();

        assertEquals(5, calculator.computePossibleTargets(king).size());

        chessboard.getSquareAt(0, 5).occupyWith(queen);

        assertEquals(16, calculator.computePossibleTargets(queen).size());
        assertEquals(2, calculator.computePossibleTargets(king).size());

        chessboard.getSquareAt(5, 5).occupyWith(rook);

        assertEquals(1, calculator.computePossibleTargets(king).size());

        chessboard.getSquareAt(2, 2).occupyWith(pawn);

        assertEquals(0, calculator.computePossibleTargets(king).size());
        assertFalse(calculator.canMove(king));
    }

    @Test
    public void pieceShouldBePinned(){
        Piece king = new Piece(1, PieceType.KING, Color.WHITE);
        Piece rook = new Piece(2, PieceType.ROOK, Color.BLACK);
        Piece knight = new Piece(3, PieceType.KNIGHT, Color.WHITE);

        chessboard.getSquareAt(0, 4).occupyWith(king);
        chessboard.getSquareAt(2, 4).occupyWith(knight);
        chessboard.getSquareAt(7, 4).occupyWith(rook);

        assertTrue(calculator.isPinned(knight));
        assertFalse(calculator.isPinned(rook));
        assertFalse(calculator.isPinned(king));

        Piece bishop = new Piece(4, PieceType.BISHOP, Color.BLACK);
        Piece pawn = new Piece(5, PieceType.PAWN, Color.WHITE);

        chessboard.getSquareAt(1, 3).occupyWith(pawn);
        chessboard.getSquareAt(4, 0).occupyWith(bishop);

        assertTrue(calculator.isPinned(pawn));
        assertFalse(calculator.isPinned(bishop));

        Piece queen = new Piece(6, PieceType.QUEEN, Color.BLACK);
        Piece pawn2 = new Piece(7, PieceType.PAWN, Color.WHITE);

        chessboard.getSquareAt(0, 5).occupyWith(pawn2);
        chessboard.getSquareAt(0, 7).occupyWith(queen);

        assertTrue(calculator.isPinned(pawn2));
        assertFalse(calculator.isPinned(queen));
    }

    @Test
    public void shouldHaveCorrectTargetAfterCheck(){
        Piece king = new Piece(1, PieceType.KING, Color.WHITE);
        Piece rook = new Piece(2, PieceType.ROOK, Color.WHITE);
        Piece queen = new Piece(3, PieceType.QUEEN, Color.BLACK);

        chessboard.getSquareAt(0, 3).occupyWith(king);
        chessboard.getSquareAt(3, 4).occupyWith(rook);
        chessboard.getSquareAt(3, 6).occupyWith(queen);

        assertEquals(1, calculator.getCheckingPieces(king).size());
        assertEquals(2, calculator.computePossibleTargets(rook).size());
        assertEquals(4, calculator.computePossibleTargets(king).size());

        Piece pawn = new Piece(4, PieceType.PAWN, Color.WHITE);
        chessboard.getSquareAt(0, 4).occupyWith(pawn);

        assertEquals(1, calculator.computePossibleTargets(pawn).size());

        chessboard.getSquareFor(queen).free();

        assertEquals(0, calculator.getCheckingPieces(king).size());
        assertEquals(2, calculator.computePossibleTargets(pawn).size());
        assertEquals(13, calculator.computePossibleTargets(rook).size());
        assertEquals(4, calculator.computePossibleTargets(king).size());
    }
}
