package be.thefluffypangolin.paysville.ui.players_names;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import be.thefluffypangolin.paysville.R;
import be.thefluffypangolin.paysville.databinding.FragmentPlayersNamesBinding;

public class PlayersNamesFragment extends Fragment {

    private PlayersNamesViewModel playersNamesViewModel;
    private FragmentPlayersNamesBinding binding;
    private TextView text;
    private LinearLayout linearLayout;
    private List<TextInputLayout> textInputLayouts;
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
        textInputLayouts = new ArrayList<>();
        fab = binding.fabPlayersNames;

        // texte de bienvenue
        text.setText(String.format(getResources().getString(R.string.text_players_names), numberOfPlayers));

        // champs pour entrer les prénoms
        for (int i = 0; i < numberOfPlayers; i++) {
            int finalI = i;
            TextInputLayout textInputLayout = new TextInputLayout(requireContext());

            textInputLayout.setHint("Joueur " + (i + 1));
            final float scale = getResources().getDisplayMetrics().density;
            int padding_8dp = (int) (5*scale + 0.5f);
            textInputLayout.setPadding(padding_8dp,padding_8dp,padding_8dp,padding_8dp);

            TextInputEditText textInputEditText = new TextInputEditText(textInputLayout.getContext());

            textInputEditText.setText(playersNamesViewModel.getPlayersNames()[i]);
            textInputEditText.addTextChangedListener(new TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override public void afterTextChanged(Editable s) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    playersNamesViewModel.getPlayersNames()[finalI] = s.toString();
                    textInputLayout.setErrorEnabled(false);
                }
            });
            textInputLayout.addView(textInputEditText);
            linearLayout.addView(textInputLayout, i);
            textInputLayouts.add(textInputLayout);
        }

        fab.setOnClickListener(v -> {
            boolean[] errors = errorsInFields();
            boolean error = false;

            // parcourt la liste pour vérifier s'il y a une erreur
            for (int i = 0; i < numberOfPlayers; i++) {
                if (errors[i]) {
                    error = true;
                    textInputLayouts.get(i).setErrorEnabled(true);
                    textInputLayouts.get(i).setError("Entrez un nom !");
                } else {
                    textInputLayouts.get(i).setErrorEnabled(false);
                }
            }

            if (error) {
                // au moins un champ est vide
                Snackbar.make(requireView(), "Vérifiez ce que vous avez entré",
                        BaseTransientBottomBar.LENGTH_SHORT)
                        .show();
            } else {
                // naviguer au fragment suivant
                String[] playersNames = playersNamesViewModel.getPlayersNames();
                PlayersNamesFragmentDirections.ActionPlayersNamesToDone action
                        = PlayersNamesFragmentDirections.actionPlayersNamesToDone(numberOfPlayers, playersNames);
                Navigation.findNavController(v).navigate(action);
            }
        });

        return binding.getRoot();
    }

    /**
     * @return une liste indiquant pour chaque champ s'il est vide
     */
    private boolean[] errorsInFields() {
        boolean[] list = new boolean[numberOfPlayers];
        for (int i = 0; i < numberOfPlayers; i++) {
            EditText editText = textInputLayouts.get(i).getEditText();
            list[i] = (editText != null && editText.getText().length() == 0);
        }
        return list;
    }
}