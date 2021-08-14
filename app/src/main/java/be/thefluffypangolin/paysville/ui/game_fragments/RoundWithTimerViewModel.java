package be.thefluffypangolin.paysville.ui.game_fragments;

import android.os.CountDownTimer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RoundWithTimerViewModel extends ViewModel {
    private CountDownTimer timer;
    private boolean timerLaunched = false;
    private MutableLiveData<Boolean> timerFinished;
    private MutableLiveData<Integer> remainingSeconds;

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

    public MutableLiveData<Boolean> isTimerFinished() {
        if (timerFinished == null)
            timerFinished = new MutableLiveData<>(false);
        return timerFinished;
    }

    public MutableLiveData<Integer> getRemainingSeconds() {
        if (remainingSeconds == null)
            remainingSeconds = new MutableLiveData<>();
        return remainingSeconds;
    }
}