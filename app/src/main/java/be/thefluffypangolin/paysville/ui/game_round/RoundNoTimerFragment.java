package be.thefluffypangolin.paysville.ui.game_round;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import org.jetbrains.annotations.NotNull;

import be.thefluffypangolin.paysville.GameActivity;
import be.thefluffypangolin.paysville.GameViewModel;
import be.thefluffypangolin.paysville.R;
import be.thefluffypangolin.paysville.databinding.FragmentGameRoundNoTimerBinding;
import be.thefluffypangolin.paysville.model.PaysVilleGame;

public class RoundNoTimerFragment extends Fragment {

    private PaysVilleGame game;
    private GameActivity activity;
    private ExtendedFloatingActionButton fab;
    private FragmentGameRoundNoTimerBinding binding;
    private TextView letter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        game = new ViewModelProvider(requireActivity()).get(GameViewModel.class).getGame();
        fab = activity.getFAB();
        binding = FragmentGameRoundNoTimerBinding.inflate(inflater, container, false);
        letter = binding.roundLetter;

        // lettre de la manche
        letter.setText(Character.toString(game.getCurrentRound().getLetter()));

        // bouton encodage
        fab.setText(R.string.encode_points);
        fab.setIconResource(R.drawable.ic_edit_24dp);
        fab.setOnClickListener(v -> {
            NavDirections action = RoundNoTimerFragmentDirections.actionRoundNoTimerToPoints();
            Navigation.findNavController(activity, R.id.nav_host_fragment_activity_game).navigate(action);
        });

        return binding.getRoot();
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        activity = (GameActivity) context;
    }
}