package be.thefluffypangolin.paysville.ui.game_finished;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import be.thefluffypangolin.paysville.GameActivity;
import be.thefluffypangolin.paysville.GameViewModel;
import be.thefluffypangolin.paysville.R;
import be.thefluffypangolin.paysville.databinding.FragmentGameFinishedBinding;
import be.thefluffypangolin.paysville.databinding.FragmentGamePointsBinding;
import be.thefluffypangolin.paysville.model.PaysVilleGame;

public class FinishedFragment extends Fragment {

    private PaysVilleGame game;
    private GameActivity activity;
    private FragmentGameFinishedBinding binding;
    private ExtendedFloatingActionButton fab;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentGameFinishedBinding.inflate(inflater, container, false);
        fab = activity.getFAB();

        fab.setIconResource(R.drawable.ic_baseline_navigate_next_24dp);
        fab.setText(R.string.winners);
        fab.setOnClickListener(v -> {
            Snackbar.make(activity.getCoordinatorLayout(),
                    game.getWinners() == null ? "oups" : game.getWinners().toString(),
                    BaseTransientBottomBar.LENGTH_SHORT)
                    .show();
        });
        return binding.getRoot();
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        activity = (GameActivity) context;
        game = new ViewModelProvider(activity).get(GameViewModel.class).getGame();
    }
}
