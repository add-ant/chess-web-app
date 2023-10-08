package it.unicam.cs.pawm.chessbackend;

import it.unicam.cs.pawm.chessbackend.model.game.match.ChessTimer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TimerTest {
    @Test
    public void timerShouldStartAndPause(){
        ChessTimer chessTimer = new ChessTimer(60000);

        chessTimer.start();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        chessTimer.pause();
        assertTrue(chessTimer.isPaused());
        assertFalse(chessTimer.isDone());
        assertEquals(50000, chessTimer.getTimeLeft());
    }

    @Test
    public void timerShouldExpire(){
        ChessTimer chessTimer = new ChessTimer(15000);

        chessTimer.start();

        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        assertTrue(chessTimer.isDone());
        assertEquals(0, chessTimer.getTimeLeft());
        assertThrows(IllegalStateException.class, chessTimer::start);
    }
}
