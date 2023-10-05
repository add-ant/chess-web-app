package it.unicam.cs.pawm.chessbackend.model.game;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A target calculator computes all the possible targets reachable by a piece of a given type from the
 * square that it is currently occupying.
 */
public class TargetsCalculator {
    private final Chessboard chessboard;

    public TargetsCalculator(Chessboard chessboard) {
        this.chessboard = chessboard;
    }

    public Set<Square> defendedSquares(Piece piece){
        Square origin = chessboard.getSquareFor(piece);
        return switch (piece.getPieceType()){
            case PAWN -> pawnDefendedSquares(piece, origin);
            case ROOK -> rookDefendedSquares(piece, origin);
            case BISHOP -> bishopDefendedSquares(piece, origin);
            case KNIGHT -> knightDefendedSquares(piece, origin);
            case QUEEN -> queenDefendedSquares(piece, origin);
            case KING -> kingDefendedSquares(piece, origin);
        };
    }

    private Set<Square> kingDefendedSquares(Piece piece, Square origin) {
        return Arrays.stream(Direction.values())
            .map(d -> chessboard.getSquareFrom(origin, d))
                .filter(Optional::isPresent)
                    .map(Optional::get)
                        .collect(Collectors.toSet());
    }

    private Set<Square> knightDefendedSquares(Piece piece, Square origin) {
        Set<Optional<Square>> possibleTargets = new HashSet<>();

        possibleTargets.add(chessboard.getSquareFrom(origin, Direction.UP, Direction.LEFT, 2, 1));
        possibleTargets.add(chessboard.getSquareFrom(origin, Direction.UP, Direction.RIGTH, 2, 1));
        possibleTargets.add(chessboard.getSquareFrom(origin, Direction.LEFT, Direction.UP, 2, 1));
        possibleTargets.add(chessboard.getSquareFrom(origin, Direction.LEFT, Direction.DOWN, 2, 1));
        possibleTargets.add(chessboard.getSquareFrom(origin, Direction.DOWN, Direction.LEFT, 2, 1));
        possibleTargets.add(chessboard.getSquareFrom(origin, Direction.DOWN, Direction.RIGTH, 2, 1));
        possibleTargets.add(chessboard.getSquareFrom(origin, Direction.RIGTH, Direction.UP, 2, 1));
        possibleTargets.add(chessboard.getSquareFrom(origin, Direction.RIGTH, Direction.DOWN, 2, 1));

        return possibleTargets.stream()
                .filter(Optional::isPresent)
                    .map(Optional::get)
                        .collect(Collectors.toSet());
    }

    private Set<Square> queenDefendedSquares(Piece piece, Square origin) {
        return Arrays.stream(Direction.values())
            .map(d -> defendedSquaresForDirection(origin, piece, d))
                .flatMap(Set::stream)
                    .collect(Collectors.toSet());
    }

    private Set<Square> bishopDefendedSquares(Piece piece, Square origin) {
        return Arrays.stream(Direction.values())
            .filter(Direction::isDiagonal)
                .map(d -> defendedSquaresForDirection(origin, piece, d))
                    .flatMap(Set::stream)
                        .collect(Collectors.toSet());
    }

    private Set<Square> rookDefendedSquares(Piece piece, Square origin) {
        return Arrays.stream(Direction.values())
            .filter(d -> !d.isDiagonal())
                .map(d -> defendedSquaresForDirection(origin, piece, d))
                    .flatMap(Set::stream)
                        .collect(Collectors.toSet());
    }

    private Set<Square> defendedSquaresForDirection(Square origin, Piece piece, Direction direction) {
        List<Square> empty = chessboard.getEmptySquaresFrom(origin, direction);
        Set<Square> defended = new HashSet<>(empty);
        Optional<Square> square;

        if (!empty.isEmpty()) {
            square = chessboard.getSquareFrom(empty.get(empty.size() - 1), direction);
        } else {
            square = chessboard.getSquareFrom(origin, direction);
        }

        if (square.isPresent()) {
            if (square.get().getPiece().isPresent()) {
                Piece p = square.get().getPiece().get();
                if (p.getPieceType().equals(PieceType.KING) && !p.getColor().equals(piece.getColor())) {
                    defended.addAll(chessboard.getEmptySquaresFrom(square.get(), direction));
                }
                defended.add(square.get());

            } else {
                defended.add(square.get());
            }
        }
        return defended;
    }

    private Set<Square> pawnDefendedSquares(Piece piece, Square origin) {
        Set<Square> defended = new HashSet<>();
        Optional<Square> right;
        Optional<Square> left;
        if (piece.getColor().equals(Color.WHITE)){
            right = chessboard.getSquareFrom(origin, Direction.UPRIGTH);
            left = chessboard.getSquareFrom(origin, Direction.UPLEFT);
        } else {
            right = chessboard.getSquareFrom(origin, Direction.DOWNRIGTH);
            left = chessboard.getSquareFrom(origin, Direction.DOWNLEFT);
        }

        right.ifPresent(defended::add);
        left.ifPresent(defended::add);

        return defended;
    }

    /**
     * Computes the possible target squares that a specific piece can reach applying its move rules.
     *
     * @param piece the piece to move.
     * @return the possible target squares that a specific piece can reach applying its move rules.
     */
    public Set<Square> computePossibleTargets(Piece piece) {
        Square origin = chessboard.getSquareFor(piece);
        return switch (piece.getPieceType()) {
            case PAWN -> computePawnTargets(piece, origin);
            case ROOK -> computeRookTargets(piece, origin);
            case BISHOP -> computeBishopTargets(piece, origin);
            case KNIGHT -> computeKnightTargets(piece, origin);
            case QUEEN -> computeQueenTargets(piece, origin);
            case KING -> computeKingTargets(piece, origin);
        };
    }

    /**
     * Returns true if the given piece has at least one possible target square to move in, false
     * otherwise.
     *
     * @param piece the piece to move.
     * @return true if the given piece has at least one possible target square to move in, false
     *         otherwise.
     */
    public boolean canMove(Piece piece){
        return computePossibleTargets(piece).size() != 0;
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
                Square current = chessboard.getSquareAt(i, j);
                if (current.getPiece().isPresent() && current.getPiece().get().getColor().equals(color))
                    pieces.add(current.getPiece().get());
            }
        }
        return pieces;
    }

    /**
     * Computes the set of all possible targets of all the pieces of a given color.
     *
     * @param color the color of the pieces.
     * @return the set of all possible targets of all the pieces of a given color.
     */
    public Set<Square> targetsOfPiecesOfColor(Color color){
        Set<Piece> pieces = getPiecesOfColor(color);

        return pieces.stream()
            .map(this::computePossibleTargets)
                .flatMap(Set::stream)
                    .collect(Collectors.toSet());
    }

    public Set<Square> defendedSquaresForPiecesOf(Color color){
        Set<Piece> pieces = getPiecesOfColor(color);

        return pieces.stream()
                .map(this::defendedSquares)
                    .flatMap(Set::stream)
                        .collect(Collectors.toSet());

    }

    private Set<Square> computeKingTargets(Piece piece, Square origin) {
        return Arrays.stream(Direction.values())
                    .map(d -> chessboard.getSquareFrom(origin, d))
                        .filter(Optional::isPresent)
                            .map(Optional::get)
                                .filter(
                                    s -> (s.isEmpty() ||
                                        (
                                            s.getPiece().isPresent() &&
                                                !s.getPiece().get().getColor().equals(piece.getColor())
                                        )) && !defendedSquaresForPiecesOf(piece.getColor().swap()).contains(s)
                                )
                                    .collect(Collectors.toSet());
    }

    private Set<Square> computeKnightTargets(Piece piece, Square origin) {
        Set<Optional<Square>> possibleTargets = new HashSet<>();

        possibleTargets.add(chessboard.getSquareFrom(origin, Direction.UP, Direction.LEFT, 2, 1));
        possibleTargets.add(chessboard.getSquareFrom(origin, Direction.UP, Direction.RIGTH, 2, 1));
        possibleTargets.add(chessboard.getSquareFrom(origin, Direction.LEFT, Direction.UP, 2, 1));
        possibleTargets.add(chessboard.getSquareFrom(origin, Direction.LEFT, Direction.DOWN, 2, 1));
        possibleTargets.add(chessboard.getSquareFrom(origin, Direction.DOWN, Direction.LEFT, 2, 1));
        possibleTargets.add(chessboard.getSquareFrom(origin, Direction.DOWN, Direction.RIGTH, 2, 1));
        possibleTargets.add(chessboard.getSquareFrom(origin, Direction.RIGTH, Direction.UP, 2, 1));
        possibleTargets.add(chessboard.getSquareFrom(origin, Direction.RIGTH, Direction.DOWN, 2, 1));

        return possibleTargets.stream()
                    .filter(Optional::isPresent)
                        .map(Optional::get)
                            .filter(
                                s -> s.isEmpty() ||
                                    (s.getPiece().isPresent() &&
                                        !s.getPiece().get().getColor().equals(piece.getColor()) &&
                                        !s.getPiece().get().getPieceType().equals(PieceType.KING)
                                    )
                            )
                                    .collect(Collectors.toSet());
    }

    private Set<Square> computeQueenTargets(Piece piece, Square origin) {
        return Arrays.stream(Direction.values())
                    .map(d -> moveTargets(piece, origin, d))
                        .flatMap(Set::stream)
                            .collect(Collectors.toSet());
    }

    private Set<Square> computeBishopTargets(Piece piece, Square origin) {
        return Arrays.stream(Direction.values())
                    .filter(Direction::isDiagonal)
                        .map(d -> moveTargets(piece, origin, d))
                            .flatMap(Set::stream)
                                .collect(Collectors.toSet());
    }

    private Set<Square> computeRookTargets(Piece piece, Square origin) {

        return Arrays.stream(Direction.values())
                    .filter(d -> !d.isDiagonal())
                        .map(d -> moveTargets(piece, origin, d))
                            .flatMap(Set::stream)
                                .collect(Collectors.toSet());
    }

    private Set<Square> moveTargets(Piece piece, Square origin, Direction direction) {
        List<Square> emptySquares = chessboard.getEmptySquaresFrom(origin, direction);
        Set<Square> targets = new HashSet<>(emptySquares);
        Optional<Square> squareToCheck;

        if (emptySquares.isEmpty()){
            squareToCheck = chessboard.getSquareFrom(origin, direction);
        } else {
            squareToCheck = chessboard.getSquareFrom(emptySquares.get(emptySquares.size() - 1), direction);
        }
        if (squareToCheck.isPresent()){
            if (squareToCheck.get().getPiece().isPresent()){
                Piece toCapture = squareToCheck.get().getPiece().get();
                if (!toCapture.getPieceType().equals(PieceType.KING)){
                    if (!toCapture.getColor().equals(piece.getColor()))
                        targets.add(squareToCheck.get());
                }
            }
        }
        return targets;
    }

    private Set<Square> computePawnTargets(Piece piece, Square origin) {
        Set<Square> targets = new HashSet<>();

        Set<Square> upTargets = computeVertical(piece, origin);
        Set<Square> diagonalTargets = computeDiagonal(piece, origin);
        targets.addAll(upTargets);
        targets.addAll(diagonalTargets);

        return targets;
    }

    private Set<Square> computeDiagonal(Piece piece, Square origin) {
        Optional<Square> right;
        Optional<Square> left;
        Set<Square> resultSet = new HashSet<>();

        if (piece.getColor().equals(Color.WHITE)) {
            right = chessboard.getSquareFrom(origin, Direction.UPRIGTH);
            left = chessboard.getSquareFrom(origin, Direction.UPLEFT);
        } else {
            right = chessboard.getSquareFrom(origin, Direction.DOWNRIGTH);
            left = chessboard.getSquareFrom(origin, Direction.DOWNLEFT);
        }

        if (right.isPresent()) {
            if (right.get().getPiece().isPresent()) {
                Piece toCapture = right.get().getPiece().get();
                if (!toCapture.getPieceType().equals(PieceType.KING)){
                    if (!right.get().getPiece().get().getColor().equals(piece.getColor()))
                        resultSet.add(right.get());
                }
            }
        }

        if (left.isPresent()) {
            if (left.get().getPiece().isPresent()) {
                Piece toCapture = left.get().getPiece().get();
                if (!toCapture.getPieceType().equals(PieceType.KING)){
                    if (!left.get().getPiece().get().getColor().equals(piece.getColor()))
                        resultSet.add(left.get());
                }
            }
        }
        return resultSet;
    }

    private Set<Square> computeVertical(Piece piece, Square origin) {
        List<Square> empty;

        if (piece.getColor().equals(Color.WHITE)) {
            empty = chessboard.getEmptySquaresFrom(origin, Direction.UP);
        } else {
            empty = chessboard.getEmptySquaresFrom(origin, Direction.DOWN);
        }

        if (!piece.hasMoved()) {
            if (empty.size() >= 2)
                return Set.of(empty.get(0), empty.get(1));
            else if (empty.size() == 1)
                return Set.of(empty.get(0));
        } else {
            if (empty.size() != 0)
                return Set.of(empty.get(0));
        }
        return Collections.emptySet();
    }


}
