package be.thefluffypangolin.paysville.ui.players_done;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import be.thefluffypangolin.paysville.R;
import be.thefluffypangolin.paysville.databinding.FragmentPlayersDoneBinding;

public class PlayersDoneFragment extends Fragment {

    private FragmentPlayersDoneBinding binding;
    private TextView text;
    private FloatingActionButton fab;

    private int numberOfPlayers;
    private String[] playersNames;

    private FABClicked listener;

    /* permet d'envoyer les données récoltées à l'activité */
    public interface FABClicked {
        void sendArgs(int number, String[] names);
        void launchGameActivity();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPlayersDoneBinding.inflate(inflater, container, false);
        fab = binding.fabPlayersDone;
        text = binding.textPlayersDone;

        numberOfPlayers = PlayersDoneFragmentArgs.fromBundle(getArguments()).getNumberOfPlayers();
        playersNames = PlayersDoneFragmentArgs.fromBundle(getArguments()).getPlayersNames();

        text.setText(String.format(getResources().getString(R.string.text_players_done),
                getPlayersNamesString(playersNames)));

        fab.setOnClickListener(v -> {
            listener.sendArgs(numberOfPlayers, playersNames);
            listener.launchGameActivity();
        });

        return binding.getRoot();
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        try {
            listener = (FABClicked) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(requireActivity().toString()
                    + " must implement FABClicked");
        }
    }

    private String getPlayersNamesString(String[] names) {
        if (names.length == 1)
            return names[0];
        else {
            StringBuilder s = new StringBuilder();
            for (int i = 0; i < names.length - 1; i++)
                s.append(names[i]).append(", ");
            s.append("et ").append(names[names.length - 1]);
            return s.toString();
        }
    }
}