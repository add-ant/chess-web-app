package it.unicam.cs.pawm.chessbackend.model.game;

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
}
