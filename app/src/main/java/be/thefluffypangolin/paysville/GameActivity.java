package be.thefluffypangolin.paysville;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.HasDefaultViewModelProviderFactory;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import be.thefluffypangolin.paysville.databinding.ActivityGameBinding;
import be.thefluffypangolin.paysville.model.GameParameters;

public class GameActivity extends AppCompatActivity implements HasDefaultViewModelProviderFactory {

    private ActivityGameBinding binding;
    private GameViewModel model;
    private ExtendedFloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // binding & ViewModel
        binding = ActivityGameBinding.inflate(getLayoutInflater());
        model = new ViewModelProvider(this).get(GameViewModel.class);
        fab = binding.fabGame;
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbarLayoutGame.toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // arguments
        Intent intent = getIntent();
        GameParameters parameters = intent.getParcelableExtra(MainActivity.KEY_GAME_PARAMETERS);
        int numberOfPlayers = intent.getIntExtra(PlayersChoiceActivity.KEY_NUMBER_OF_PLAYERS, -1);
        String[] playersNames = intent.getStringArrayExtra(PlayersChoiceActivity.KEY_PLAYERS_NAMES);

        // gérer le bouton Back
        GameActivity activity = this;
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                displayExitMessage();
            }
        });

        // initialisation du jeu, s'il n'existe pas encore
        model.init(parameters, numberOfPlayers, playersNames);
    }

    public ExtendedFloatingActionButton getFAB() {
        return fab;
    }

    public CoordinatorLayout getCoordinatorLayout() {
        return binding.coordinatorLayoutGameActivity;
    }

    @Override
    public boolean onSupportNavigateUp() {
        displayExitMessage();
        return false;
    }

    private void displayExitMessage() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle(R.string.leave_game_title)
                .setMessage(R.string.leave_game_msg)
                .setPositiveButton(R.string.leave, (dialog, which) -> this.finish())
                .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }
}