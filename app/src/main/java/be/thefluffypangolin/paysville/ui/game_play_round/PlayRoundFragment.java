package be.thefluffypangolin.paysville.ui.game_play_round;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import be.thefluffypangolin.paysville.GameActivity;
import be.thefluffypangolin.paysville.GameViewModel;
import be.thefluffypangolin.paysville.R;
import be.thefluffypangolin.paysville.databinding.FragmentGamePlayRoundBinding;

public class PlayRoundFragment extends Fragment {

    private GameViewModel gameModel;
    private GameActivity activity;
    private ExtendedFloatingActionButton fab;
    private FragmentGamePlayRoundBinding binding;
    private TextView textRoundNumber;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGamePlayRoundBinding.inflate(inflater, container, false);
        fab = activity.getFAB();
        gameModel = new ViewModelProvider(requireActivity()).get(GameViewModel.class);
        textRoundNumber = binding.playRoundNumber;

        // numéro de la manche
        textRoundNumber.setText(String.format(getResources().getString(R.string.text_round_number),
                gameModel.getGame().getActualRoundNumber()));

        // bouton lancer
        fab.setText(R.string.play_round);
        fab.setIconResource(R.drawable.ic_play_arrow_24dp);
        fab.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "suivant", Toast.LENGTH_SHORT).show();
        });
        
        return binding.getRoot();
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        activity = ((GameActivity) context);
    }
}