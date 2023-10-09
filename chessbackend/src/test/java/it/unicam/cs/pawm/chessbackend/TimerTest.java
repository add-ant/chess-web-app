package it.unicam.cs.pawm.chessbackend;

import it.unicam.cs.pawm.chessbackend.model.game.match.SimpleTimer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TimerTest {
    @Test
    public void timerShouldStartAndPause(){
        SimpleTimer simpleTimer = new SimpleTimer(60000);

        simpleTimer.start();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        simpleTimer.pause();
        assertTrue(simpleTimer.isPaused());
        assertFalse(simpleTimer.isDone());
        assertEquals(50000, simpleTimer.getTimeLeft());
    }

    @Test
    public void timerShouldExpire(){
        SimpleTimer simpleTimer = new SimpleTimer(15000);

        simpleTimer.start();

        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        assertTrue(simpleTimer.isDone());
        assertEquals(0, simpleTimer.getTimeLeft());
        assertThrows(IllegalStateException.class, simpleTimer::start);
    }
}
