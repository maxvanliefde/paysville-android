package be.thefluffypangolin.paysville.ui.players_names;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Arrays;
import java.util.List;

import be.thefluffypangolin.paysville.R;
import be.thefluffypangolin.paysville.databinding.FragmentPlayersNamesBinding;

public class PlayersNamesFragment extends Fragment {

    private PlayersNamesViewModel playersNamesViewModel;
    private FragmentPlayersNamesBinding binding;
    private TextView text;
    private LinearLayout linearLayout;
    private FloatingActionButton fab;
    private int numberOfPlayers;

    public static PlayersNamesFragment newInstance() {
        return new PlayersNamesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        System.out.println("created");
        numberOfPlayers = PlayersNamesFragmentArgs.fromBundle(getArguments()).getNumberOfPlayers();
        playersNamesViewModel = new ViewModelProvider(this).get(PlayersNamesViewModel.class);
        if (playersNamesViewModel.getPlayersNames() == null)
            playersNamesViewModel.initializePlayersNamesList(numberOfPlayers);
        binding = FragmentPlayersNamesBinding.inflate(inflater, container, false);
        text = binding.textPlayersNames;
        linearLayout = binding.layoutPlayersNames;
        fab = binding.fabPlayersNames;

        // texte de bienvenue
        text.setText(String.format(getResources().getString(R.string.text_players_names), numberOfPlayers));

        // champs pour entrer les prénoms
        for (int i = 0; i < numberOfPlayers; i++) {
            int finalI = i;
            TextInputLayout textInputLayout = new TextInputLayout(requireContext());
            textInputLayout.setHint("Joueur " + (i + 1));

            TextInputEditText textInputEditText = new TextInputEditText(textInputLayout.getContext());
            textInputEditText.setText(playersNamesViewModel.getPlayersNames()[i]);
            textInputEditText.addTextChangedListener(new TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override public void afterTextChanged(Editable s) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    playersNamesViewModel.getPlayersNames()[finalI] = s.toString();
                }
            });

            textInputLayout.addView(textInputEditText);
            linearLayout.addView(textInputLayout, i);
        }

        fab.setOnClickListener(v -> {
            // naviguer au fragment suivant
            String[] playersNames = playersNamesViewModel.getPlayersNames();
            PlayersNamesFragmentDirections.ActionPlayersNamesToDone action
                    = PlayersNamesFragmentDirections.actionPlayersNamesToDone(numberOfPlayers, playersNames);
            Navigation.findNavController(v).navigate(action);
        });

        return binding.getRoot();
    }
}