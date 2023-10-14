package it.unicam.cs.pawm.chessbackend.model.game.match;

import java.util.Timer;
import java.util.TimerTask;

/**
 * A chess timer is associated to a player during a chess match.
 *
 * When the player's turn starts, the timer associated to that player starts (if it is his first turn) or
 * resume. Every time the player makes a move, the timer is paused and the player's turn ends.
 *
 * If a timer ends, the player associated to that timer lost the game because of time.
 */
public class SimpleTimer {
    private long timeLeft;

    private boolean paused;

    private final Timer timer;

    public SimpleTimer(long timeLeft) {
        this.timeLeft = timeLeft;
        this.timer = new Timer();
        this.paused = true;
    }

    public long getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(long timeLeft) {
        this.timeLeft = timeLeft;
    }

    public void start(){
        if (isDone())
            throw new IllegalStateException("Timer expired, could not be started");

        paused = false;
        TimerTask decreaseOneSec = new TimerTask() {
            @Override
            public void run() {
                if (getTimeLeft() >= 1000 && !isDone())
                    setTimeLeft(timeLeft -= 1000);
                else if (isPaused()){

                } else {
                    timer.cancel();
                }
            }
        };
        timer.schedule(decreaseOneSec, 0,  1000);
    }

    public boolean isDone() {
        return timeLeft == 0;
    }

    public boolean isPaused() {
        return paused;
    }

    public void pause(){
        paused = true;
    }

    public void stop(){
        timeLeft = 0;
    }


}
