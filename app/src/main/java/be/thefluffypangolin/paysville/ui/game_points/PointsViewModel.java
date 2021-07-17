package be.thefluffypangolin.paysville.ui.game_points;

import androidx.lifecycle.ViewModel;

public class PointsViewModel extends ViewModel {
    private String[] playersScores;

    public void init(int size) {
        playersScores = new String[size];
        for (int i = 0; i < size; i++) {
            playersScores[i] = "";
        }
    }

    public String[] getPlayersScores() {
        return playersScores;
    }
}