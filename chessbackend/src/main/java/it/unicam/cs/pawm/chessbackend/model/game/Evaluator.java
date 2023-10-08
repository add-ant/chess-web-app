package it.unicam.cs.pawm.chessbackend.model.game;

import java.util.Set;

/**
 * This class is responsible for the evaluation of chess moves during a game.
 */
public class Evaluator implements MoveEvaluator{
    private final TargetsCalculator calculator;

    public Evaluator(Chessboard board) {
        this.calculator = new TargetsCalculator(board);
    }

    public TargetsCalculator getCalculator() {
        return calculator;
    }

    @Override
    public MoveEffect evaluate(Move move) {
        Piece pieceToMove = move.getPiece();
        Set<Piece> enemies = calculator.getChessboard().getPiecesOfColor(pieceToMove.getColor().swap());
        Set<Piece> pieces = calculator.getChessboard().getPiecesOfColor(pieceToMove.getColor());

        Piece enemyKing = enemies.stream()
            .filter(p -> p.getPieceType().equals(PieceType.KING))
            .findFirst()
            .orElseThrow();

        Piece king = enemies.stream()
            .filter(p -> p.getPieceType().equals(PieceType.KING))
            .findFirst()
            .orElseThrow();

        if (calculator.computePossibleTargets(pieceToMove).contains(move.getTarget())) {
            boolean isTargetEmptyBeforeMove = calculator.getChessboard().getSquareAt(
                move.getTarget().getRow(),
                move.getTarget().getColumn()
            ).isEmpty();

            calculator.getChessboard().evolve(move);

            if (pieces.stream().noneMatch(calculator::canMove) && calculator.getCheckingPieces(king).isEmpty())
                return MoveEffect.DRAW;

            if (!pieceToMove.hasMoved())
                pieceToMove.setMoved();

            if (calculator.getCheckingPieces(enemyKing).contains(pieceToMove)) {
                if (enemies.stream().noneMatch(calculator::canMove))
                    return MoveEffect.CHECKMATE;
                else return MoveEffect.CHECK;
            } else if (isTargetEmptyBeforeMove) {
                return MoveEffect.MOVE;
            } else {
                return MoveEffect.CAPTURE;
            }
        }
        return MoveEffect.ILLEGAL;
    }


}
