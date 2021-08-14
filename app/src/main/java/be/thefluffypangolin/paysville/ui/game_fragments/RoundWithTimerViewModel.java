package be.thefluffypangolin.paysville.ui.game_fragments;

import android.os.CountDownTimer;

import androidx.lifecycle.ViewModel;

public class RoundWithTimerViewModel extends ViewModel {
    private boolean timerLaunched = false;
    private boolean timerFinished = false;
    private CountDownTimer timer;

    public boolean isTimerLaunched() {
        return timerLaunched;
    }

    public void setTimerLaunchedTrue() {
        this.timerLaunched = true;
    }

    public CountDownTimer getTimer() {
        return timer;
    }

    public void setTimer(CountDownTimer timer) {
        this.timer = timer;
    }

    public boolean isTimerFinished() {
        return timerFinished;
    }

    public void setTimerFinishedTrue() {
        timerFinished = true;
    }
}