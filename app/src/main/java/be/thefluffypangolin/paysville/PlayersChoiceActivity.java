package be.thefluffypangolin.paysville;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.NumberPicker;
import android.widget.Toast;

import java.util.Arrays;

import be.thefluffypangolin.paysville.databinding.ActivityPlayersChoiceBinding;
import be.thefluffypangolin.paysville.ui.players_done.PlayersDoneFragment;

public class PlayersChoiceActivity extends AppCompatActivity
        implements PlayersDoneFragment.FABClicked {

    private ActivityPlayersChoiceBinding binding;

    private int numberOfPlayers;
    private String[] playersNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlayersChoiceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setTitle("Sélection des joueurs");

        // Récupérer les paramètres
//        Intent intent = getIntent();
//        String msg = intent.getStringExtra("GAME_PARAMETERS_STRING");
//        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void sendNumberOfPlayers(int number) {
        numberOfPlayers = number;
    }

    @Override
    public void sendPlayersNames(String[] names) {
        playersNames = names;
    }

    @Override
    public void launchNewActivity() {
        Toast.makeText(this, Integer.toString(numberOfPlayers) + Arrays.toString(playersNames), Toast.LENGTH_LONG)
                .show();
    }
}