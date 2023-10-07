package it.unicam.cs.pawm.chessbackend;

import it.unicam.cs.pawm.chessbackend.model.game.Color;
import it.unicam.cs.pawm.chessbackend.model.game.Piece;
import it.unicam.cs.pawm.chessbackend.model.game.PieceFactory;
import it.unicam.cs.pawm.chessbackend.model.game.PieceType;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PieceFactoryTest {
    @Test
    public void shouldReturnCorrectPieces(){
        Set<Piece> whitePieces = PieceFactory.getSet(Color.WHITE);
        Set<Piece> blackPieces = PieceFactory.getSet(Color.BLACK);

        assertEquals(16, whitePieces.size());
        assertEquals(8, whitePieces.stream().filter(p -> p.getPieceType().equals(PieceType.PAWN)).count());
        assertEquals(2, whitePieces.stream().filter(p -> p.getPieceType().equals(PieceType.KNIGHT)).count());
        assertEquals(2, whitePieces.stream().filter(p -> p.getPieceType().equals(PieceType.ROOK)).count());
        assertEquals(2, whitePieces.stream().filter(p -> p.getPieceType().equals(PieceType.BISHOP)).count());
        assertEquals(1, whitePieces.stream().filter(p -> p.getPieceType().equals(PieceType.KING)).count());
        assertEquals(1, whitePieces.stream().filter(p -> p.getPieceType().equals(PieceType.QUEEN)).count());

        assertEquals(16, whitePieces.stream().map(Piece::getId).distinct().count());
        assertEquals(16, blackPieces.stream().map(Piece::getId).distinct().count());
        assertEquals(
            32,
            Stream.concat(whitePieces.stream(), blackPieces.stream()).map(Piece::getId).distinct().count()
        );
    }
}
