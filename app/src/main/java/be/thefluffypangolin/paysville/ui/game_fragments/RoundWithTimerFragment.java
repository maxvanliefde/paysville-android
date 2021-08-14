package be.thefluffypangolin.paysville.ui.game_fragments;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import org.jetbrains.annotations.NotNull;

import be.thefluffypangolin.paysville.GameActivity;
import be.thefluffypangolin.paysville.GameViewModel;
import be.thefluffypangolin.paysville.R;
import be.thefluffypangolin.paysville.databinding.FragmentGameRoundWithTimerBinding;
import be.thefluffypangolin.paysville.model.PaysVilleGame;

public class RoundWithTimerFragment extends Fragment {

    private PaysVilleGame game;
    private GameActivity activity;
    private ExtendedFloatingActionButton fab;
    private FragmentGameRoundWithTimerBinding binding;
    private RoundWithTimerViewModel model;
    private LinearProgressIndicator progressBar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        game = new ViewModelProvider(requireActivity()).get(GameViewModel.class).getGame();
        model = new ViewModelProvider(this).get(RoundWithTimerViewModel.class);
        fab = activity.getFAB();
        binding = FragmentGameRoundWithTimerBinding.inflate(inflater, container, false);
        progressBar = binding.progressBarTimer;

        // gestion du texte et du timer
        if (!model.isTimerLaunched()){
            setNewTimer();
            setTextLetter();
            fab.hide(new ExtendedFloatingActionButton.OnChangedCallback() {
                @Override
                public void onHidden(ExtendedFloatingActionButton extendedFab) {
                    super.onHidden(extendedFab);
                    customizeFAB();
                }
            });
            launchTimer();
        }
        if (!model.isTimerFinished()){
            setTextLetter();
            customizeFAB();
        } else {
            setTextTimerFinished();
            customizeFAB();
        }

        return binding.getRoot();
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        activity = (GameActivity) context;
    }

    private void launchTimer() {
        model.setTimerLaunchedTrue();
        progressBar.setMax(game.getParameters().getTimerDuration());
        progressBar.setProgressCompat(progressBar.getMax(), false);
        model.getTimer().start();
    }

    private void customizeFAB() {
        fab.setText(R.string.encode_points);
        fab.setIconResource(R.drawable.ic_edit_24dp);
        fab.setOnClickListener(v -> {
            NavDirections action = RoundWithTimerFragmentDirections.actionRoundWithTimerToPoints();
            Navigation.findNavController(activity, R.id.nav_host_fragment_activity_game).navigate(action);
        });
    }

    private void setTextTimerFinished() {
        binding.textAboveLetter.setText(R.string.text_round_times_up);
        binding.roundLetter.setText("⏲");
    }

    private void setTextLetter() {
        binding.textAboveLetter.setText(R.string.text_round_letter_is);
        binding.roundLetter.setText(Character.toString(game.getCurrentRound().getLetter()));
    }

    private void setNewTimer() {
        model.setTimer(new CountDownTimer(game.getParameters().getTimerDuration() * 1000L, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                progressBar.setProgressCompat((int) millisUntilFinished / 1000, true);
            }

            @Override
            public void onFinish() {
                model.setTimerFinishedTrue();
                setTextTimerFinished();
                fab.show();
            }
        });
    }
}