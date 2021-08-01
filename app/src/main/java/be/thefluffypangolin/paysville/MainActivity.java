package be.thefluffypangolin.paysville;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.Set;

import be.thefluffypangolin.paysville.databinding.ActivityMainBinding;
import be.thefluffypangolin.paysville.model.GameParameters;
import be.thefluffypangolin.paysville.ui.main_fragments.LaunchGameDialogFragment;

public class MainActivity extends AppCompatActivity implements LaunchGameDialogFragment.LaunchGameDialogListener {

    private ActivityMainBinding binding;

    public static final String KEY_GAME_PARAMETERS = "GAME_PARAMETERS_ARG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_settings, R.id.navigation_about)
                .build();
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);
        NavController navController;
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
            NavigationUI.setupWithNavController(binding.navView, navController);
        }
    }

    @Override
    public void onDialogLaunchClick(DialogFragment dialog, GameParameters parameters) {
        // lance une partie
        Intent intent = new Intent(this, PlayersChoiceActivity.class);
        intent.putExtra(KEY_GAME_PARAMETERS, parameters);
        startActivity(intent);
    }

    @Override
    public void onDialogModifyClick(DialogFragment dialog) {
        // redirige vers l'écran Paramètres et affiche un petit message
        Snackbar.make(binding.container, "Retournez sur l'onglet Jouer dès que vous aurez choisi vos paramètres", BaseTransientBottomBar.LENGTH_LONG)
                .show();
        binding.navView.setSelectedItemId(R.id.navigation_settings);
    }

    @Override
    public void onGameNotReadyException(DialogFragment dialog, GameParameters.GameNotReadyException e) {
        // redirige vers l'écran Paramètres car un ou plusieurs paramètres sont incorrects
        dialog.dismiss();
        Set<String> reasons = e.getReasons();
        if (reasons == null) {
            Snackbar.make(binding.container, "Vérifiez les paramètres de jeu", BaseTransientBottomBar.LENGTH_LONG).show();
        } else {
            StringBuilder message = new StringBuilder("Vérifiez les paramètres de jeu : ");
            for (String reason : reasons) {
                message.append(reason).append(", ");
            }
            Snackbar.make(binding.container,
                        message.delete(message.length()-2, message.length()-1).toString(),
                        BaseTransientBottomBar.LENGTH_LONG)
                    .show();
        }
        binding.navView.setSelectedItemId(R.id.navigation_settings);
    }
}