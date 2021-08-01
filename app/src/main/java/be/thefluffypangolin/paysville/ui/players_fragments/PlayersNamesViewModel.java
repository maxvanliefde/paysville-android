package be.thefluffypangolin.paysville.ui.players_fragments;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class PlayersNamesViewModel extends ViewModel {
    private String[] playersNames;

    public void initializePlayersNamesList(int size) {
        playersNames = new String[size];
        for (int i = 0; i < size; i++) {
            playersNames[i] = "";
        }
    }

    public String[] getPlayersNames() {
        return playersNames;
    }
}