package it.unicam.cs.pawm.chessbackend.model.game;

import java.util.Objects;

/**
 * This class is responsible for the evaluation of chess moves during a game.
 */
public class Evaluator implements MoveEvaluator{
    private final Chessboard board;

    public Evaluator(Chessboard board) {
        this.board = Objects.requireNonNull(board);
    }

    @Override
    public MoveEffect evaluate(Move move) {
        Square origin = move.getOrigin();
        Square target = move.getTarget();
        Piece piece = move.getPiece();

        if(isValidTarget(piece, target)){
            return switch (piece.getPieceType()){
                case PAWN -> null;
                case ROOK -> null;
                case BISHOP -> null;
                case KNIGHT -> null;
                case QUEEN -> null;
                case KING -> null;
            };
        }
        return MoveEffect.ILLEGAL;
    }

    /**
     * Checks if the target square is occupied by some piece of the same color of the piece to
     * move to that square.
     *
     * @param piece the piece to move.
     * @param target the target square.
     * @return true if the target is empty or occupied by a piece of different color, false otherwise.
     */
    private boolean isValidTarget(Piece piece, Square target) {
        if (target.getPiece().isPresent()){
            return !piece.getColor().equals(target.getPiece().get().getColor());
        }
        return true;
    }




}
