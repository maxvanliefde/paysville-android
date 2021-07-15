package be.thefluffypangolin.paysville.ui.players_names;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import be.thefluffypangolin.paysville.R;
import be.thefluffypangolin.paysville.databinding.FragmentPlayersNamesBinding;

public class PlayersNamesFragment extends Fragment {

    private PlayersNamesViewModel playersNamesViewModel;
    private FragmentPlayersNamesBinding binding;
    private CoordinatorLayout coordinatorLayout;
    private TextView text;
    private LinearLayout linearLayout;
    private List<TextInputLayout> textInputLayouts;
    private FloatingActionButton fab;
    private int numberOfPlayers;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        numberOfPlayers = PlayersNamesFragmentArgs.fromBundle(getArguments()).getNumberOfPlayers();
        playersNamesViewModel = new ViewModelProvider(this).get(PlayersNamesViewModel.class);
        if (playersNamesViewModel.getPlayersNames() == null)
            playersNamesViewModel.initializePlayersNamesList(numberOfPlayers);
        binding = FragmentPlayersNamesBinding.inflate(inflater, container, false);
        coordinatorLayout = binding.coordinatorPlayersNames;
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
            textInputEditText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
            textInputEditText.setOnFocusChangeListener((v, hasFocus) -> {if (!hasFocus) hideKeyboard(v);});
            textInputLayout.addView(textInputEditText);
            linearLayout.addView(textInputLayout, i);
            textInputLayouts.add(textInputLayout);
        }

        // bouton suivant
        fab.setOnClickListener(v -> {
            boolean error = false;
            boolean[] empties = errorsInFields();
            Set<Integer> duplicates = null;
            try {
                duplicates = findDuplicates(textInputLayouts.stream()
                        .map(x -> x.getEditText().getText().toString())
                        .collect(Collectors.toList()));
            } catch (NullPointerException e) {
                error = true;
            }

            // cache le clavier
            hideKeyboard(fab);

            // parcourt la liste pour vérifier si un champ est vide
            for (int i = 0; i < numberOfPlayers; i++) {
                textInputLayouts.get(i).clearFocus();
                if (empties[i]) {
                    error = true;
                    textInputLayouts.get(i).setErrorEnabled(true);
                    textInputLayouts.get(i).setError("Entrez un nom !");
                } else {
                    textInputLayouts.get(i).setErrorEnabled(false);
                }
            }

            // vérifie les noms en doublons
            if (duplicates != null) {
                for (Integer i : duplicates) {
                    error = true;
                    textInputLayouts.get(i).setErrorEnabled(true);
                    textInputLayouts.get(i).setError("Veuillez choisir nom différent !");
                }                
            }

            if (error) {
                Snackbar.make(coordinatorLayout, "Vérifiez ce que vous avez entré",
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
            list[i] = (editText == null || editText.getText().length() == 0);
        }
        return list;
    }


    private Set<Integer> findDuplicates(List<String> list) {
        final Set<Integer> setToReturn = new HashSet<>();
        final Set<String> setTemp = new HashSet<>();

        for (int i = 0; i < numberOfPlayers; i++) {
            String s = list.get(i);
            if (!s.equals("") && !setTemp.add(s))
                setToReturn.add(i);
        }

        return setToReturn;
    }

    /**
     * Cache le clavier
     * @param v la vue qui fait la demande
     */
    private void hideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
}