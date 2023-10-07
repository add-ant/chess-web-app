package it.unicam.cs.pawm.chessbackend;

import it.unicam.cs.pawm.chessbackend.model.game.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EvaluatorTest {
    private static Evaluator evaluator;

    @BeforeEach
    public void initialize(){
        Chessboard board = new Chessboard();
        evaluator = new Evaluator(board);
    }

    @Test
    public void shouldBeSimpleMove(){
        Piece whiteKing = new Piece(1, PieceType.KING, Color.WHITE);
        Piece blackKing = new Piece(2, PieceType.KING, Color.BLACK);
        Piece whiteRook = new Piece(3, PieceType.ROOK, Color.WHITE);
        Piece blackRook = new Piece(4, PieceType.ROOK, Color.BLACK);

        evaluator.getCalculator().getChessboard().getSquareAt(0, 4).occupyWith(whiteKing);
        evaluator.getCalculator().getChessboard().getSquareAt(7, 3).occupyWith(blackKing);
        evaluator.getCalculator().getChessboard().getSquareAt(0, 7).occupyWith(whiteRook);
        evaluator.getCalculator().getChessboard().getSquareAt(7, 0).occupyWith(blackRook);

        Move whiteRookMove = new Move(
            evaluator.getCalculator().getChessboard().getSquareAt(0, 7),
            evaluator.getCalculator().getChessboard().getSquareAt(4, 7)
        );

        Move blackRookMove = new Move(
            evaluator.getCalculator().getChessboard().getSquareAt(7, 0),
            evaluator.getCalculator().getChessboard().getSquareAt(2, 0)
        );

        assertEquals(MoveEffect.MOVE, evaluator.evaluate(whiteRookMove));
        assertEquals(MoveEffect.MOVE, evaluator.evaluate(blackRookMove));

        assertEquals(evaluator.getCalculator().getChessboard().getSquareAt(4, 7),
            evaluator.getCalculator().getChessboard().getSquareFor(whiteRook));

        assertEquals(evaluator.getCalculator().getChessboard().getSquareAt(2, 0),
            evaluator.getCalculator().getChessboard().getSquareFor(blackRook));
    }

    @Test
    public void moveShouldBeIllegal(){
        Piece whiteKing = new Piece(1, PieceType.KING, Color.WHITE);
        Piece blackKing = new Piece(2, PieceType.KING, Color.BLACK);
        Piece whiteKnight = new Piece(3, PieceType.KNIGHT, Color.WHITE);
        Piece blackRook = new Piece(4, PieceType.ROOK, Color.BLACK);
        Piece blackQueen = new Piece(5, PieceType.QUEEN, Color.BLACK);

        evaluator.getCalculator().getChessboard().getSquareAt(0, 4).occupyWith(whiteKing);
        evaluator.getCalculator().getChessboard().getSquareAt(7, 3).occupyWith(blackKing);
        evaluator.getCalculator().getChessboard().getSquareAt(3, 4).occupyWith(whiteKnight);
        evaluator.getCalculator().getChessboard().getSquareAt(7, 4).occupyWith(blackRook);
        evaluator.getCalculator().getChessboard().getSquareAt(0, 5).occupyWith(blackQueen);


        Move knightMove = new Move(
            evaluator.getCalculator().getChessboard().getSquareAt(3, 4),
            evaluator.getCalculator().getChessboard().getSquareAt(1, 5)
        );

        Move kingMove = new Move(
            evaluator.getCalculator().getChessboard().getSquareAt(0, 4),
            evaluator.getCalculator().getChessboard().getSquareAt(1, 5)
        );

        Move rookMove = new Move(
            evaluator.getCalculator().getChessboard().getSquareAt(7, 4),
            evaluator.getCalculator().getChessboard().getSquareAt(6, 5)
        );

        assertEquals(MoveEffect.ILLEGAL, evaluator.evaluate(knightMove));
        assertEquals(MoveEffect.ILLEGAL, evaluator.evaluate(kingMove));
        assertEquals(MoveEffect.ILLEGAL, evaluator.evaluate(rookMove));
    }

    @Test
    public void shouldBeACaptureMove(){
        Piece whiteKing = new Piece(1, PieceType.KING, Color.WHITE);
        Piece blackKing = new Piece(2, PieceType.KING, Color.BLACK);
        Piece whiteKnight = new Piece(3, PieceType.KNIGHT, Color.WHITE);
        Piece blackRook = new Piece(4, PieceType.ROOK, Color.BLACK);

        evaluator.getCalculator().getChessboard().getSquareAt(0, 4).occupyWith(whiteKing);
        evaluator.getCalculator().getChessboard().getSquareAt(7, 3).occupyWith(blackKing);
        evaluator.getCalculator().getChessboard().getSquareAt(3, 4).occupyWith(whiteKnight);
        evaluator.getCalculator().getChessboard().getSquareAt(1, 5).occupyWith(blackRook);

        Move knightMove = new Move(
            evaluator.getCalculator().getChessboard().getSquareAt(3, 4),
            evaluator.getCalculator().getChessboard().getSquareAt(1, 5)
        );

        assertEquals(MoveEffect.CAPTURE, evaluator.evaluate(knightMove));
        Square target = evaluator.getCalculator().getChessboard().getSquareAt(1, 5);
        assertTrue(target.getPiece().isPresent());
        assertEquals(whiteKnight, target.getPiece().get());
    }

    @Test
    public void shouldBeCheckMove(){
        Piece whiteKing = new Piece(1, PieceType.KING, Color.WHITE);
        Piece blackQueen = new Piece(2, PieceType.QUEEN, Color.BLACK);

        evaluator.getCalculator().getChessboard().getSquareAt(0, 4).occupyWith(whiteKing);
        evaluator.getCalculator().getChessboard().getSquareAt(3, 5).occupyWith(blackQueen);

        Move blackQueenMove = new Move(
            evaluator.getCalculator().getChessboard().getSquareAt(3, 5),
            evaluator.getCalculator().getChessboard().getSquareAt(1, 5)
        );

        assertEquals(MoveEffect.CHECK, evaluator.evaluate(blackQueenMove));
    }

    @Test
    public void shouldBeCheckmateMove(){
        Piece whiteKing = new Piece(1, PieceType.KING, Color.WHITE);
        Piece blackQueen = new Piece(2, PieceType.QUEEN, Color.BLACK);
        Piece blackRook = new Piece(3, PieceType.ROOK, Color.BLACK);

        evaluator.getCalculator().getChessboard().getSquareAt(0, 4).occupyWith(whiteKing);
        evaluator.getCalculator().getChessboard().getSquareAt(3, 4).occupyWith(blackQueen);
        evaluator.getCalculator().getChessboard().getSquareAt(7, 4).occupyWith(blackRook);

        Move blackQueenMove = new Move(
            evaluator.getCalculator().getChessboard().getSquareAt(3, 4),
            evaluator.getCalculator().getChessboard().getSquareAt(1, 4)
        );

        assertEquals(MoveEffect.CHECKMATE, evaluator.evaluate(blackQueenMove));
    }
}
