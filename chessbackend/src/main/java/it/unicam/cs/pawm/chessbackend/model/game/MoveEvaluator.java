package it.unicam.cs.pawm.chessbackend.model.game;

import java.util.List;

/**
 * A move evaluator is responsible to verify the effect of a specific move.
 */

public interface MoveEvaluator {
    /**
     * Evaluates a move and returns the effect of performing that move. If the effect returned is
     * <code>MoveEffect.ILLEGAL</code> the move cannot be performed.
     *
     * @param move the move to evaluate.
     * @return the effect of performing that move.
     */
    MoveEffect evaluate(Move move);
}
