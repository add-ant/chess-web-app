package it.unicam.cs.pawm.chessbackend.model.game;

import java.util.*;
import java.util.stream.IntStream;

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

    public void initializeBoard(Set<Piece> firstSet, Set<Piece> secondSet){
        if (firstSet.stream().allMatch(p -> p.getColor().equals(Color.WHITE))){
            if (secondSet.stream().allMatch(p -> p.getColor().equals(Color.BLACK))){
                initializeTeam(firstSet, Color.WHITE);
                initializeTeam(secondSet, Color.BLACK);
            }
        } else if (secondSet.stream().allMatch(p -> p.getColor().equals(Color.WHITE))){
            initializeTeam(firstSet, Color.BLACK);
            initializeTeam(secondSet, Color.WHITE);
        }
    }

    private void initializeTeam(Set<Piece> pieceSet, Color color) {
        int pawnRow = 1;
        int piecesRow = 0;
        int pawnCol = 0;

        if (color.equals(Color.BLACK)){
            pawnRow = 6;
            piecesRow = 7;
        }

        for (Piece p :
            pieceSet) {
            switch (p.getPieceType()){
                case PAWN -> chessboard[pawnRow][pawnCol++].occupyWith(p);
                case ROOK -> {
                    if (getSquareAt(piecesRow, 0).isEmpty()) {
                        chessboard[piecesRow][0].occupyWith(p);
                    } else
                        chessboard[piecesRow][7].occupyWith(p);
                }
                case KNIGHT -> {
                    if (getSquareAt(piecesRow, 1).isEmpty()) {
                        chessboard[piecesRow][1].occupyWith(p);
                    } else
                        chessboard[piecesRow][6].occupyWith(p);
                }
                case BISHOP -> {
                    if (getSquareAt(piecesRow, 2).isEmpty()) {
                        chessboard[piecesRow][2].occupyWith(p);
                    } else
                        chessboard[piecesRow][5].occupyWith(p);
                }
                case QUEEN -> chessboard[piecesRow][3].occupyWith(p);
                case KING -> chessboard[piecesRow][4].occupyWith(p);
            }
        }
    }

    public Square getSquareAt(int row, int column){
        return chessboard[row][column];
    }

    public void evolve(Move move){
        move.perform();
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
     * Returns all the pieces of a given color that are present in the chessboard.
     *
     * @param color the color of the pieces to get.
     * @return all the pieces of a given color that are present in the chessboard.
     */
    public Set<Piece> getPiecesOfColor(Color color){
        Set<Piece> pieces = new HashSet<>();

        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                Square current = getSquareAt(i, j);
                if (current.getPiece().isPresent() && current.getPiece().get().getColor().equals(color))
                    pieces.add(current.getPiece().get());
            }
        }
        return pieces;
    }

    Optional<Piece> getPiece(PieceType type, Color color){

        for (int k = 0; k < 8; k++){
            for (int j = 0; j < 8; j++){
                Square current = getSquareAt(k, j);
                if (current.getPiece().isPresent()){
                    if (current.getPiece().get().getColor().equals(color) && current.getPiece().get().getPieceType().equals(type))
                        return current.getPiece();
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Given a direction, returns the adjacent square to the origin square passed in that direction.
     *
     * @param origin the origin square.
     * @param direction the direction.
     * @return the adjacent square to the origin in that direction (if exists).
     */
    public Optional<Square> getSquareFrom(Square origin, Direction direction){
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

    public Optional<Square> getSquareFrom(Square origin, Direction firstDirection, Direction secondDirection,
                                          int firstDistance, int secondDistance){
        Optional<Square> intermediate = Optional.of(origin);
        Optional<Square> toFound = Optional.empty();
        int firstCount = 0;
        int secondCount = 0;

        while (firstCount < firstDistance){
            if (intermediate.isPresent()){
                intermediate = getSquareFrom(intermediate.get(), firstDirection);
                firstCount++;
            } else
                break;
        }

        if (intermediate.isPresent()){
            while (secondCount < secondDistance){
                toFound = getSquareFrom(intermediate.get(), secondDirection);
                secondCount++;
            }
        }
        return toFound;
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
