package be.thefluffypangolin.paysville.ui.game_fragments;

import androidx.lifecycle.Observer;
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

        // gestion du timer et de la ProgressBar
        int timerDuration = game.getCurrentRound().getTimerDuration();
        progressBar = binding.progressBarTimer;
        progressBar.setMax(timerDuration);
        progressBar.setProgressCompat(progressBar.getMax(), false);
        if (!model.isTimerLaunched())
            setAndLaunchNewTimer(timerDuration);

        // gestion du texte et du FAB
        final Observer<Boolean> booleanObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean timerFinished) {
                if (!timerFinished) {
                    setTextLetter();
                    fab.hide(new ExtendedFloatingActionButton.OnChangedCallback() {
                        @Override
                        public void onHidden(ExtendedFloatingActionButton extendedFab) {
                            super.onHidden(extendedFab);
                            customizeFAB();
                        }
                    });
                } else {
                    setTextTimerFinished();
                    customizeFAB();
                    fab.show();
                }
            }
        };
        model.isTimerFinished().observe(requireActivity(), booleanObserver);

        // gestion de l'avancement de la ProgressBar
        final Observer<Integer> integerObserver = i -> progressBar.setProgressCompat(i, true);
        model.getRemainingSeconds().observe(requireActivity(), integerObserver);

        return binding.getRoot();
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        activity = (GameActivity) context;
    }

    private void setAndLaunchNewTimer(int duration) {
        model.setTimer(new CountDownTimer(duration * 1000L, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                model.getRemainingSeconds().setValue((int) millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                model.isTimerFinished().setValue(true);
            }
        }.start());
        model.setTimerLaunchedTrue();
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
}