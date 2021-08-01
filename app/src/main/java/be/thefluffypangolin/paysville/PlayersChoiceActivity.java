package be.thefluffypangolin.paysville;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import be.thefluffypangolin.paysville.databinding.ActivityPlayersChoiceBinding;
import be.thefluffypangolin.paysville.model.GameParameters;
import be.thefluffypangolin.paysville.ui.players_fragments.PlayersDoneFragment;

public class PlayersChoiceActivity extends AppCompatActivity
        implements PlayersDoneFragment.FABClicked {

    private ActivityPlayersChoiceBinding binding;

    private GameParameters gameParameters;
    private int numberOfPlayers;
    private String[] playersNames;

    public static String KEY_NUMBER_OF_PLAYERS = "NUMBER_OF_PLAYERS_ARG";
    public static String KEY_PLAYERS_NAMES = "PLAYERS_NAMES_ARG";
    public static ActivityResultLauncher<Intent> startForResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlayersChoiceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setTitle("Sélection des joueurs");

        // Récupérer les paramètres
        Intent intent = getIntent();
        gameParameters = intent.getParcelableExtra(MainActivity.KEY_GAME_PARAMETERS);

        // fermer cette activité lors de la fin du jeu
        startForResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                activityResult -> this.finish());
    }

    @Override
    public void sendArgs(int number, String[] names) {
        numberOfPlayers = number;
        playersNames = names;
    }

    @Override
    public void launchGameActivity() {
        Intent intent = new Intent(this, GameActivity.class)
                .putExtra(MainActivity.KEY_GAME_PARAMETERS, gameParameters)
                .putExtra(KEY_NUMBER_OF_PLAYERS, numberOfPlayers)
                .putExtra(KEY_PLAYERS_NAMES, playersNames);
        startForResult.launch(intent);
    }
}