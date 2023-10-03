package it.unicam.cs.pawm.chessbackend.model.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This class represents a chessboard. It consists of a set of 64 squares organized in rows and
 * columns.
 *
 * The columns are numbered from 'a' to 'h' and the rows are numbered from 1 to 8 so that each
 * square is uniquely identified by a pair of indexes (e.g. the bottom-left square is identified
 * by the pair ('a', 1)).
 * A chessboard must have the bottom-right square ('h', 1) and the top left square ('a', 8) as
 * white squares to be considered valid.
 *
 * The chessboard takes track of the pieces positions as the game progress: after each move of
 * one of the two player the chessboard configuration changes.
 */
public class Chessboard {
    private final Square[][] chessboard = new Square[8][8];

    public Chessboard() {
        createChessboard();
    }

    private void createChessboard() {
        Color current = Color.BLACK;
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                chessboard[i][j] = new Square(i, j, current);
                if (j != 7)
                    current = current.swap();
            }
        }
    }

    public Square[][] getChessboard() {
        return chessboard;
    }

    public Square getSquareAt(int row, int column){
        return chessboard[row][column];
    }

    public Square getSquareFor(Piece piece){
        Square toReturn = null;
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                if (chessboard[i][j].getPiece().isPresent()){
                    if (chessboard[i][j].getPiece().get().equals(piece))
                        toReturn = getSquareAt(i, j);
                }
            }
        }
        return toReturn;
    }

    public List<Square> getEmptySquaresFrom(Square origin, Direction direction){
        List<Square> emptySquares = new ArrayList<>();
        Optional<Square> current = getSquareFrom(origin, direction);

        while (current.isPresent() && current.get().isEmpty()){
            emptySquares.add(current.get());
            current = getSquareFrom(current.get(), direction);
        }
        return emptySquares;
    }


    /**
     * Given a direction, returns the adjacent square to the origin square passed in that direction.
     *
     * @param origin the origin square.
     * @param direction the direction.
     * @return the adjacent square to the origin in that direction (if exists).
     */
    private Optional<Square> getSquareFrom(Square origin, Direction direction){
        return switch (direction){
            case UP -> getUpSquare(origin);
            case DOWN -> getDownSquare(origin);
            case LEFT -> getLeftSquare(origin);
            case RIGTH -> getRightSquare(origin);
            case UPRIGTH -> getUpRightSquare(origin);
            case UPLEFT -> getUpLeftSquare(origin);
            case DOWNRIGTH -> getDownRightSquare(origin);
            case DOWNLEFT -> getDownLeftSquare(origin);
        };
    }

    private Optional<Square> getDownLeftSquare(Square origin) {
        if(!(origin.getRow() == 0) && !(origin.getColumn() == 0))
            return Optional.of(getSquareAt(origin.getRow() - 1, origin.getColumn() - 1));
        return Optional.empty();
    }

    private Optional<Square> getDownRightSquare(Square origin) {
        if (!(origin.getRow() == 0) && !(origin.getColumn() == 7))
            return Optional.of(getSquareAt(origin.getRow() - 1, origin.getColumn() + 1));
        return Optional.empty();
    }

    private Optional<Square> getUpLeftSquare(Square origin) {
        if (!(origin.getRow() == 7) && !(origin.getColumn() == 0))
            return Optional.of(getSquareAt(origin.getRow() + 1, origin.getColumn() - 1));
        return Optional.empty();
    }

    private Optional<Square> getUpRightSquare(Square origin) {
        if (!(origin.getRow() == 7) && !(origin.getColumn() == 7))
            return Optional.of(getSquareAt(origin.getRow() + 1, origin.getColumn() + 1));
        return Optional.empty();
    }

    private Optional<Square> getRightSquare(Square origin) {
        if (origin.getColumn() < 7)
            return Optional.of(getSquareAt(origin.getRow(), origin.getColumn() + 1));
        return Optional.empty();
    }

    private Optional<Square> getLeftSquare(Square origin) {
        if (origin.getColumn() > 0)
            return Optional.of(getSquareAt(origin.getRow(), origin.getColumn() - 1));
        return Optional.empty();
    }

    private Optional<Square> getDownSquare(Square origin) {
        if (origin.getRow() > 0)
            return Optional.of(getSquareAt(origin.getRow() - 1, origin.getColumn()));
        return Optional.empty();
    }

    private Optional<Square> getUpSquare(Square origin) {
        if (origin.getRow() < 7)
            return Optional.of(getSquareAt(origin.getRow() + 1, origin.getColumn()));
        return Optional.empty();
    }
}
