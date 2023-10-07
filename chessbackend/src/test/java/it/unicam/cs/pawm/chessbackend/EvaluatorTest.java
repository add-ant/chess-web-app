package it.unicam.cs.pawm.chessbackend;

import it.unicam.cs.pawm.chessbackend.model.game.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
