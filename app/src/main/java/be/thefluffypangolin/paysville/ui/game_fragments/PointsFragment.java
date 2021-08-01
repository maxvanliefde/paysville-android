package be.thefluffypangolin.paysville.ui.game_fragments;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import be.thefluffypangolin.paysville.GameActivity;
import be.thefluffypangolin.paysville.GameViewModel;
import be.thefluffypangolin.paysville.R;
import be.thefluffypangolin.paysville.databinding.FragmentGamePointsBinding;
import be.thefluffypangolin.paysville.model.PaysVilleGame;

public class PointsFragment extends Fragment {

    private PointsViewModel model;
    private PaysVilleGame game;
    private GameActivity activity;
    private ExtendedFloatingActionButton fab;
    private FragmentGamePointsBinding binding;
    private LinearLayoutCompat layout;
    private int numberOfPlayers;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        game = new ViewModelProvider(requireActivity()).get(GameViewModel.class).getGame();
        numberOfPlayers = game.getNumberOfPlayers();
        model = new ViewModelProvider(this).get(PointsViewModel.class);
        if (model.getPlayersScores() == null) model.init(game.getNumberOfPlayers());
        binding = FragmentGamePointsBinding.inflate(inflater, container, false);
        layout = binding.layoutGamePoints;
        fab = activity.getFAB();

        for (int i = 0; i < numberOfPlayers; i++) {
            int finalI = i;

            TextInputLayout textInputLayout = new TextInputLayout(requireContext());
            textInputLayout.setHint(game.getPlayers()[i].getName());

            TextInputEditText textInputEditText = new TextInputEditText(textInputLayout.getContext());
            textInputEditText.setText(model.getPlayersScores()[i]);
            textInputEditText.addTextChangedListener(new TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override public void afterTextChanged(Editable s) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    model.getPlayersScores()[finalI] = s.toString();
                }
            });
            textInputEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
            textInputLayout.addView(textInputEditText);
            layout.addView(textInputLayout, i);
        }

        // bouton enregistrer
        fab.setText(R.string.save);
        fab.setIconResource(R.drawable.ic_save_24dp);
        fab.setOnClickListener(this::convertPointsAndContinue);

        return binding.getRoot();
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        activity = (GameActivity) context;
    }

    private void convertPointsAndContinue(View v) {
        try {
            String[] stringArray = model.getPlayersScores();
            int[] intArray = Arrays.stream(stringArray).mapToInt(Integer::parseInt).toArray();
            game.addAllScoresToCurrentRound(intArray);
            NavDirections action = PointsFragmentDirections.actionPointsToVerify();
            Navigation.findNavController(activity, R.id.nav_host_fragment_activity_game).navigate(action);
        } catch (NumberFormatException e) {
            Snackbar.make(activity.getCoordinatorLayout(),
                    "Vérifiez que vous avez correctement encodé tous les points",
                    BaseTransientBottomBar.LENGTH_SHORT)
                    .show();
        }
    }
}