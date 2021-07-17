package be.thefluffypangolin.paysville.ui.game_round;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import org.jetbrains.annotations.NotNull;

import be.thefluffypangolin.paysville.GameActivity;
import be.thefluffypangolin.paysville.GameViewModel;
import be.thefluffypangolin.paysville.databinding.FragmentGameRoundWithTimerBinding;
import be.thefluffypangolin.paysville.model.PaysVilleGame;

public class RoundWithTimerFragment extends Fragment {

    private PaysVilleGame game;
    private GameActivity activity;
    private ExtendedFloatingActionButton fab;
    private FragmentGameRoundWithTimerBinding binding;
    private RoundWithTimerViewModel model;

    // TODO : implement the timer

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        game = new ViewModelProvider(requireActivity()).get(GameViewModel.class).getGame();
        model = new ViewModelProvider(this).get(RoundWithTimerViewModel.class);
        fab = activity.getFAB();
        binding = FragmentGameRoundWithTimerBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        activity = (GameActivity) context;
    }
}