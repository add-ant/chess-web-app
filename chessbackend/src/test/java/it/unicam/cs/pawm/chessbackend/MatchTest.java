package it.unicam.cs.pawm.chessbackend;

import it.unicam.cs.pawm.chessbackend.model.game.Chessboard;
import it.unicam.cs.pawm.chessbackend.model.game.Color;
import it.unicam.cs.pawm.chessbackend.model.game.Move;
import it.unicam.cs.pawm.chessbackend.model.game.match.Match;
import it.unicam.cs.pawm.chessbackend.model.game.match.MatchEndingResult;
import it.unicam.cs.pawm.chessbackend.model.game.match.Player;
import it.unicam.cs.pawm.chessbackend.model.game.match.PlayerPair;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;



import static org.junit.jupiter.api.Assertions.*;

public class MatchTest {
    private static Match match;

    @BeforeEach
    public void initializeMatch(){
        Chessboard board = new Chessboard();
        Player white = new Player(Color.WHITE, "ant", 600);
        Player black = new Player(Color.BLACK, "ben", 700);
        PlayerPair players = new PlayerPair(white, black);
        match = new Match(0, 60000, players);
    }

    @Test
    public void shouldStartWithWhite(){
        match.start();

        assertFalse(match.isEnded());
        assertNotNull(match.getClock().getCurrentlyPlaying());
        assertNotNull(match.getClock().getCurrentlyPlaying());
        assertEquals(Color.WHITE, match.getClock().getCurrentlyPlaying().getTeam());
        assertEquals(Color.BLACK, match.getClock().getCurrentlyWaiting().getTeam());
    }

    @Test
    public void shouldSwitchPlayerAfterMove(){
        Chessboard board = match.getMoveEvaluator().getCalculator().getChessboard();
        Move whitePawnMove = new Move(
            board.getSquareAt(1, 3), board.getSquareAt(3, 3)
        );

        match.start();

        match.onMove(whitePawnMove);

        assertFalse(match.isEnded());
        assertEquals(Color.BLACK, match.getClock().getCurrentlyPlaying().getTeam());
        assertEquals(Color.WHITE, match.getClock().getCurrentlyWaiting().getTeam());
    }

    @Test
    public void shouldNotBePossibleToMoveOtherPlayerPieces(){
        Chessboard board = match.getMoveEvaluator().getCalculator().getChessboard();
        Move blackPawnMove = new Move(
            board.getSquareAt(6, 3), board.getSquareAt(4, 3)
        );

        match.start();

        assertThrows(IllegalArgumentException.class, () -> match.onMove(blackPawnMove));
    }

    @Test
    public void shouldBeEndedOnTime(){
        match.start();

        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        assertTrue(match.isEnded());
        assertTrue(match.getEnding().isPresent());
        assertEquals(MatchEndingResult.TIME, match.getEnding().get());
    }

    @Test
    public void shouldNotBePossibleToMovePiecesIfEnded(){
        Chessboard board = match.getMoveEvaluator().getCalculator().getChessboard();
        Move whitePawnMove = new Move(
            board.getSquareAt(1, 3), board.getSquareAt(3, 3)
        );
        match.start();

        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        assertTrue(match.isEnded());
        assertThrows(IllegalStateException.class, () -> match.onMove(whitePawnMove));
    }

    @Test
    public void shouldEndWithCheckmate(){
        Chessboard board = match.getMoveEvaluator().getCalculator().getChessboard();

        Move whitePawnMove1 = new Move(
            board.getSquareAt(1, 6), board.getSquareAt(3, 6)
        );
        Move whitePawnMove2 = new Move(
            board.getSquareAt(1, 5), board.getSquareAt(2, 5)
        );
        Move blackPawnMove = new Move(
            board.getSquareAt(6, 4), board.getSquareAt(5, 4)
        );
        Move foolCheckmate = new Move(
            board.getSquareAt(7, 3), board.getSquareAt(3, 7)
        );
        match.start();

        match.onMove(whitePawnMove1);
        match.onMove(blackPawnMove);
        match.onMove(whitePawnMove2);
        match.onMove(foolCheckmate);

        assertTrue(match.isEnded());
        assertTrue(match.getWinner().isPresent());
        assertTrue(match.getEnding().isPresent());
        assertEquals(match.getBlackPlayer(), match.getWinner().get());
        assertEquals(MatchEndingResult.CHECKMATE_END, match.getEnding().get());
    }


}
