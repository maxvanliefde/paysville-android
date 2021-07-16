package be.thefluffypangolin.paysville;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.HasDefaultViewModelProviderFactory;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import be.thefluffypangolin.paysville.databinding.ActivityGameBinding;
import be.thefluffypangolin.paysville.model.GameParameters;
import be.thefluffypangolin.paysville.model.PaysVilleGame;

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

        // arguments
        Intent intent = getIntent();
        GameParameters parameters = intent.getParcelableExtra(MainActivity.KEY_GAME_PARAMETERS);
        int numberOfPlayers = intent.getIntExtra(PlayersChoiceActivity.KEY_NUMBER_OF_PLAYERS, -1);
        String[] playersNames = intent.getStringArrayExtra(PlayersChoiceActivity.KEY_PLAYERS_NAMES);

        // initialisation du jeu, s'il n'existe pas encore
        model.init(parameters, numberOfPlayers, playersNames);
    }

    public ExtendedFloatingActionButton getFAB() {
        return fab;
    }
}