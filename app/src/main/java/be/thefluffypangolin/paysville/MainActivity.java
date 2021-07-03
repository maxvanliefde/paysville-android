package be.thefluffypangolin.paysville;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import be.thefluffypangolin.paysville.databinding.ActivityMainBinding;
import be.thefluffypangolin.paysville.ui.home.LaunchGameDialogFragment;

public class MainActivity extends AppCompatActivity implements LaunchGameDialogFragment.LaunchGameDialogListener {

    private ActivityMainBinding binding;

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
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    @Override
    public void onDialogLaunchClick(DialogFragment dialog) {
        // lance une partie
        Toast.makeText(this, "c'est parti", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDialogModifyClick(DialogFragment dialog) {
        // redirige vers l'écran Paramètres et affiche un petit message
        Snackbar.make(binding.container, "Retournez sur l'onglet Jouer dès que vous aurez choisi vos paramètres", BaseTransientBottomBar.LENGTH_LONG)
                .show();
        binding.navView.setSelectedItemId(R.id.navigation_settings);
    }

}