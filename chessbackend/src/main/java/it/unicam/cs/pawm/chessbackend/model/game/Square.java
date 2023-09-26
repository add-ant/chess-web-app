package it.unicam.cs.pawm.chessbackend.model.game;

import java.util.Objects;
import java.util.Optional;

/**
 * This class represents a square in the chessboard.
 *
 * A square has a specific position in the chessboard, a specific color and can be occupied with
 * a chess piece in a given moment in time or alternatively be empty.
 */
public class Square {
    private final int row;
    private final int column;
    private final Color color;
    private Piece piece;

    public Square(int row, int column, Color color) {
        this.row = getIfValid(row);
        this.column = getIfValid(column);
        this.color = color;
    }

    private int getIfValid(int index) {
        if (index >= 0 && index < 8)
            return index;
        else
            throw new IllegalArgumentException("Incorrect index for row or column");
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public Color getColor() {
        return color;
    }

    boolean isEmpty(){
        return piece == null;
    }

    /**
     * Returns the piece currently occupying this square. If the square is empty, an
     * <code>Optional.empty()</code> is returned.
     *
     * @return the piece currently occupying this square, if the square is not empty, otherwise
     *         an empty optional.
     */
    Optional<Piece> getPiece(){
        if (isEmpty())
            return Optional.empty();
        return Optional.of(piece);
    }

    /**
     * Occupies this square with a given piece: if this square is already occupied, the piece
     * previously occupying this square is returned, otherwise an <code>Optional.empty()</code>
     * is returned.
     *
     * @param piece the piece used to occupy this square.
     * @return the piece previously occupying this square (if the square was occupied before
     *         this operation).
     */
    Optional<Piece> occupyWith(Piece piece){
        if (getPiece().isPresent()){
            Piece previous = getPiece().get();
            this.piece = Objects.requireNonNull(piece);
            return Optional.of(previous);
        }
        this.piece = Objects.requireNonNull(piece);
        return Optional.empty();
    }


}
