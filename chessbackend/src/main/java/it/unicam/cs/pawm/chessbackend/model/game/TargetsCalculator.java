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

    public Set<Square> computePossibleTargets(Piece piece) {
        Square origin = chessboard.getSquareFor(piece);
        return switch (piece.getPieceType()) {
            case PAWN -> computePawnTargets(piece, origin);
            case ROOK -> computeRookTargets(piece, origin);
            case BISHOP -> computeBishopTargets(piece, origin);
            case KNIGHT -> computeKnightTargets(piece, origin);
            case QUEEN -> computeQueenTargets(piece, origin);
            case KING -> null;
        };
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
                                        !s.getPiece().get().getColor().equals(piece.getColor()))
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
        Optional<Square> toCheck;

        if (emptySquares.isEmpty()){
            toCheck = chessboard.getSquareFrom(origin, direction);
        } else {
            toCheck = chessboard.getSquareFrom(emptySquares.get(emptySquares.size() - 1), direction);
        }
        if (toCheck.isPresent()){
            if (toCheck.get().getPiece().isPresent()){
                if (!toCheck.get().getPiece().get().getColor().equals(piece.getColor()))
                    targets.add(toCheck.get());
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
                if (!right.get().getPiece().get().getColor().equals(piece.getColor()))
                    resultSet.add(right.get());
            }
        }

        if (left.isPresent()) {
            if (left.get().getPiece().isPresent()) {
                if (!left.get().getPiece().get().getColor().equals(piece.getColor()))
                    resultSet.add(left.get());
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
