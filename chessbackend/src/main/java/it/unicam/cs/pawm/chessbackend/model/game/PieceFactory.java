package it.unicam.cs.pawm.chessbackend.model.game;

import java.util.HashSet;
import java.util.Set;

/**
 * A piece factory is responsible for the creation of a set of piece used by a player during a chess match.
 * Every piece has a unique identifier and the same color.
 * A set of pieces contains the following types of pieces:
 *      - 8 pawns
 *      - 2 rooks
 *      - 2 knights
 *      - 2 bishops
 *      - 1 queen
 *      - 1 king.
 */
public class PieceFactory {
    private static int id = 0;

    /**
     * Returns a complete set of piece of given color that can be used by one player during a match.
     *
     * @param color the color of the pieces of the set.
     * @return a complete set of piece of given color.
     */
    public static Set<Piece> getSet(Color color){
        Set<Piece> pieces = new HashSet<>();
        for (int i = 1; i <= 16; i++){
            if (i <= 8)
                pieces.add(new Piece(id++, PieceType.PAWN, color));
            if (i > 8 && i <= 10)
                pieces.add(new Piece(id++, PieceType.ROOK, color));
            if (i > 10 && i <= 12)
                pieces.add(new Piece(id++, PieceType.KNIGHT, color));
            if (i > 12 && i <= 14)
                pieces.add(new Piece(id++, PieceType.BISHOP, color));
            if (i == 15)
                pieces.add(new Piece(id++, PieceType.QUEEN, color));
            if (i == 16)
                pieces.add(new Piece(id++, PieceType.KING, color));
        }
        return pieces;
    }

    /**
     * Returns a new piece of a given type and color.
     *
     * @param type the type of the new piece.
     * @param color the color of the new piece.
     * @return a new piece of a given type and color.
     */
    public static Piece get(PieceType type, Color color){
        return new Piece(id++, type, color);
    }
}
