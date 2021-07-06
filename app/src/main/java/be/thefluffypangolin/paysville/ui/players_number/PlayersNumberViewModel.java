package be.thefluffypangolin.paysville.ui.players_number;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PlayersNumberViewModel extends ViewModel {
    private MutableLiveData<Integer> numberOfPlayers;

    public MutableLiveData<Integer> getNumberOfPlayers() {
        if (numberOfPlayers == null)
            numberOfPlayers = new MutableLiveData<>();
        return numberOfPlayers;
    }
}