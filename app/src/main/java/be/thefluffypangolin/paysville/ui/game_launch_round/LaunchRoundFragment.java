package be.thefluffypangolin.paysville.ui.game_launch_round;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import org.jetbrains.annotations.NotNull;

import be.thefluffypangolin.paysville.GameActivity;
import be.thefluffypangolin.paysville.GameViewModel;
import be.thefluffypangolin.paysville.R;
import be.thefluffypangolin.paysville.databinding.FragmentGameLaunchRoundBinding;
import be.thefluffypangolin.paysville.model.PaysVilleGame;

public class LaunchRoundFragment extends Fragment {

    private PaysVilleGame game;
    private GameActivity activity;
    private ExtendedFloatingActionButton fab;
    private FragmentGameLaunchRoundBinding binding;
    private TextView textRoundNumber;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGameLaunchRoundBinding.inflate(inflater, container, false);
        fab = activity.getFAB();
        game = new ViewModelProvider(requireActivity()).get(GameViewModel.class).getGame();
        textRoundNumber = binding.playRoundNumber;

        // numéro de la manche
        textRoundNumber.setText(String.format(getResources().getString(R.string.text_round_number),
                game.getCurrentRoundNumber()));

        // bouton lancer
        fab.setText(R.string.play_round);
        fab.setIconResource(R.drawable.ic_play_arrow_24dp);
        fab.setOnClickListener(v -> {
            if (game.getParameters().isTimerOn()) {
                NavDirections actionWithTimer = LaunchRoundFragmentDirections.actionLaunchToRoundWithTimer();
                Navigation.findNavController(activity, R.id.nav_host_fragment_activity_game).navigate(actionWithTimer);
            } else {
                NavDirections actionNoTimer = LaunchRoundFragmentDirections.actionLaunchToRoundNoTimer();
                Navigation.findNavController(activity, R.id.nav_host_fragment_activity_game).navigate(actionNoTimer);
            }
        });
        
        return binding.getRoot();
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        activity = ((GameActivity) context);
    }
}