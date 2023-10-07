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

    public Chessboard getChessboard() {
        return chessboard;
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

        if (isPinned(piece))
            return Collections.emptySet();

        if (!piece.getPieceType().equals(PieceType.KING)){
            Piece king;
            if(chessboard.getPiece(PieceType.KING, piece.getColor()).isPresent()){
                king = chessboard.getPiece(PieceType.KING, piece.getColor()).get();
                if (!getCheckingPieces(Objects.requireNonNull(king)).isEmpty()){
                    return targetsIfCheck(piece, origin, king);
                } else {
                    return switch (piece.getPieceType()) {
                        case PAWN -> computePawnTargets(piece, origin);
                        case ROOK -> computeRookTargets(piece, origin);
                        case BISHOP -> computeBishopTargets(piece, origin);
                        case KNIGHT -> computeKnightTargets(piece, origin);
                        case QUEEN -> computeQueenTargets(piece, origin);
                        case KING -> computeKingTargets(piece, origin);
                    };
                }
            }
        }

        return switch (piece.getPieceType()) {
            case PAWN -> computePawnTargets(piece, origin);
            case ROOK -> computeRookTargets(piece, origin);
            case BISHOP -> computeBishopTargets(piece, origin);
            case KNIGHT -> computeKnightTargets(piece, origin);
            case QUEEN -> computeQueenTargets(piece, origin);
            case KING -> computeKingTargets(piece, origin);
        };
    }

    private Set<Square> targetsIfCheck(Piece piece, Square origin, Piece king) {
        if (getCheckingPieces(Objects.requireNonNull(king)).size() >= 2)
            return Collections.emptySet();
        else if (getCheckingPieces(Objects.requireNonNull(king)).size() == 1){
            Piece checking = getCheckingPieces(king).get(0);
            Square checkingSquare = chessboard.getSquareFor(checking);
            Optional<Square> toDefend = defendedSquares(king).stream()
                .filter(s -> computePossibleTargets(checking).contains(s)).findFirst();
            return targetsIfCheckOfPiece(piece, origin, toDefend, checkingSquare);
        }
        return null;
    }

    private Set<Square> targetsIfCheckOfPiece(Piece piece, Square origin, Optional<Square> toDefend, Square checkingSquare) {
        Set<Square> targets = new HashSet<>();
        if (toDefend.isPresent()){
            switch (piece.getPieceType()){
                case PAWN -> {
                    if (computePawnTargets(piece, origin).contains(toDefend.get()))
                        targets.add(toDefend.get());
                    if (computePawnTargets(piece, origin).contains(checkingSquare))
                        targets.add(checkingSquare);
                }
                case KNIGHT -> {
                    if (computeKnightTargets(piece, origin).contains(toDefend.get()))
                        targets.add(toDefend.get());
                    if (computeKnightTargets(piece, origin).contains(checkingSquare))
                        targets.add(checkingSquare);
                }case QUEEN -> {
                    if (computeQueenTargets(piece, origin).contains(toDefend.get()))
                        targets.add(toDefend.get());
                    if (computeQueenTargets(piece, origin).contains(checkingSquare))
                        targets.add(checkingSquare);
                }case BISHOP -> {
                    if (computeBishopTargets(piece, origin).contains(toDefend.get()))
                        targets.add(toDefend.get());
                    if (computeBishopTargets(piece, origin).contains(checkingSquare))
                        targets.add(checkingSquare);
                }case ROOK -> {
                    if (computeRookTargets(piece, origin).contains(toDefend.get()))
                        targets.add(toDefend.get());
                    if (computeRookTargets(piece, origin).contains(checkingSquare))
                        targets.add(checkingSquare);
                }
            }
        } else {
            switch (piece.getPieceType()){
                case PAWN -> {
                    if (computePawnTargets(piece, origin).contains(checkingSquare))
                        targets.add(checkingSquare);
                }
                case KNIGHT -> {
                    if (computeKnightTargets(piece, origin).contains(checkingSquare))
                        targets.add(checkingSquare);
                }case QUEEN -> {
                    if (computeQueenTargets(piece, origin).contains(checkingSquare))
                        targets.add(checkingSquare);
                }case BISHOP -> {
                    if (computeBishopTargets(piece, origin).contains(checkingSquare))
                        targets.add(checkingSquare);
                }case ROOK -> {
                    if (computeRookTargets(piece, origin).contains(checkingSquare))
                        targets.add(checkingSquare);
                }
            }
        }
        return targets;
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
     * Tells if the king is checked. If it is checked, a list of the pieces that are checking the king
     * is returned, otherwise, an empty list is returned.
     *
     * @param piece the king.
     * @return the list of pieces checking the king.
     */
    public List<Piece> getCheckingPieces(Piece piece){
        if (!piece.getPieceType().equals(PieceType.KING))
            throw new IllegalArgumentException("Only the king could be checked.");
        Square kingSquare = chessboard.getSquareFor(piece);
        Set<Square> kingDefended = defendedSquares(piece);
        List<Piece> checking = new ArrayList<>();
        Set<Piece> enemies = chessboard.getPiecesOfColor(piece.getColor().swap());

        for (Piece p: enemies) {
            if (!isPinned(p)){
                switch (p.getPieceType()){
                    case PAWN -> {
                        if (defendedSquares(p).contains(kingSquare))
                            checking.add(p);
                    }
                    case KNIGHT -> {
                        if (computeKnightTargets(p, chessboard.getSquareFor(p)).contains(kingSquare))
                            checking.add(p);
                    }
                    case BISHOP -> {
                        if (computeBishopTargets(p, chessboard.getSquareFor(p)).stream()
                            .filter(kingDefended::contains).anyMatch(Square::isEmpty))
                            checking.add(p);
                    }
                    case ROOK -> {
                        if (computeRookTargets(p, chessboard.getSquareFor(p)).stream()
                            .filter(kingDefended::contains).anyMatch(Square::isEmpty))
                            checking.add(p);
                    }
                    case QUEEN -> {
                        if (computeQueenTargets(p, chessboard.getSquareFor(p)).stream()
                            .filter(kingDefended::contains).anyMatch(Square::isEmpty))
                            checking.add(p);
                    }
                }
            }
        }
        return checking;
    }

    public Set<Piece> getPinnedPiecesBy(Piece piece){
        return switch (piece.getPieceType()){
            case QUEEN -> checkPinnedByQueen(piece);
            case BISHOP -> checkPinnedByBishop(piece);
            case ROOK -> checkPinnedByRook(piece);
            default -> throw new IllegalStateException("Unexpected value: " + piece.getPieceType());
        };
    }

    private Set<Piece> checkPinnedByRook(Piece piece) {
        return Arrays.stream(Direction.values())
            .filter(d -> !d.isDiagonal())
                .map(d -> getPinnedPieceOnDirection(piece, d))
                    .filter(Optional::isPresent)
                        .map(Optional::get)
                            .collect(Collectors.toSet());
    }

    private Set<Piece> checkPinnedByBishop(Piece piece) {
        return Arrays.stream(Direction.values())
            .filter(Direction::isDiagonal)
                .map(d -> getPinnedPieceOnDirection(piece, d))
                    .filter(Optional::isPresent)
                        .map(Optional::get)
                            .collect(Collectors.toSet());
    }

    private Set<Piece> checkPinnedByQueen(Piece piece) {
        return Arrays.stream(Direction.values())
            .map(d -> getPinnedPieceOnDirection(piece, d))
                .filter(Optional::isPresent)
                    .map(Optional::get)
                        .collect(Collectors.toSet());
    }

    private Optional<Piece> getPinnedPieceOnDirection(Piece piece, Direction direction){
        Square origin = chessboard.getSquareFor(piece);
        Optional<Square> current = chessboard.getSquareFrom(origin, direction);
        List<Piece> pieces = new ArrayList<>();

        while (current.isPresent()){
            if (current.get().getPiece().isPresent()){
                if (!current.get().getPiece().get().getColor().equals(piece.getColor())){
                    pieces.add(current.get().getPiece().get());
                    current = chessboard.getSquareFrom(current.get(), direction);
                } else
                    break;
            } else {
                current = chessboard.getSquareFrom(current.get(), direction);
            }
        }

        if (pieces.size() >= 2 && pieces.get(1).getPieceType().equals(PieceType.KING))
            return Optional.of(pieces.get(0));
        return Optional.empty();
    }

    public boolean isPinned(Piece piece){
        if (piece.getPieceType().equals(PieceType.KING))
            return false;

        return chessboard.getPiecesOfColor(piece.getColor().swap()).stream()
            .filter(
                p -> p.getPieceType().equals(PieceType.BISHOP)
                    || p.getPieceType().equals(PieceType.QUEEN) || p.getPieceType().equals(PieceType.ROOK)
            ).map(this::getPinnedPiecesBy)
                .flatMap(Set::stream)
                    .anyMatch(p -> p.equals(piece));
    }


    /**
     * Computes the set of all possible targets of all the pieces of a given color.
     *
     * @param color the color of the pieces.
     * @return the set of all possible targets of all the pieces of a given color.
     */
    public Set<Square> targetsOfPiecesOfColor(Color color){
        Set<Piece> pieces = chessboard.getPiecesOfColor(color);

        return pieces.stream()
            .map(this::computePossibleTargets)
                .flatMap(Set::stream)
                    .collect(Collectors.toSet());
    }

    public Set<Square> defendedSquaresForPiecesOf(Color color){
        Set<Piece> pieces = chessboard.getPiecesOfColor(color);

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
