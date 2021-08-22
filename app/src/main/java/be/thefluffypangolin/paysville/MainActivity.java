package be.thefluffypangolin.paysville;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
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
    private MenuItem toggleThemeItem;

    public static final String KEY_GAME_PARAMETERS = "GAME_PARAMETERS_ARG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbarLayoutMain.toolbar);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        toggleThemeItem = menu.getItem(1);
        ThemeHelper.loadTheme(this, toggleThemeItem);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.toolbar_action_dark_theme) {
            ThemeHelper.modifyTheme(this, ThemeHelper.DARK_THEME, toggleThemeItem);
            return true;
        } else if (itemId == R.id.toolbar_action_light_theme) {
            ThemeHelper.modifyTheme(this, ThemeHelper.LIGHT_THEME, toggleThemeItem);
            return true;
        } else if (itemId == R.id.toolbar_action_default_theme) {
            ThemeHelper.modifyTheme(this, ThemeHelper.DEFAULT_THEME, toggleThemeItem);
            return true;
        }
        return super.onOptionsItemSelected(item);
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
        // redirige vers l'écran paramètres car l'utilisateur l'a voulu
        Snackbar.make(binding.container, getString(R.string.return_when_ready), BaseTransientBottomBar.LENGTH_LONG)
                .setAnchorView(binding.navView)
                .show();
        binding.navView.setSelectedItemId(R.id.navigation_settings);
    }

    @Override
    public void onGameNotReadyException(DialogFragment dialog, GameParameters.GameNotReadyException e) {
        // redirige vers l'écran Paramètres car un ou plusieurs paramètres sont incorrects
        dialog.dismiss();
        Set<String> reasons = e.getReasons();
        CharSequence text;
        if (reasons == null) {
            text = getString(R.string.verify_game_parameters);
        } else {
            StringBuilder message = new StringBuilder(getString(R.string.verify_game_parameters)).append(" : ");
            for (String reason : reasons) {
                message.append(reason).append(", ");
            }
            text = message.delete(message.length()-2, message.length()-1).toString();
        }
        binding.navView.setSelectedItemId(R.id.navigation_settings);
        Snackbar.make(binding.container, text, BaseTransientBottomBar.LENGTH_LONG)
                .setAnchorView(binding.navView)
                .show();
    }
}