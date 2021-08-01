package be.thefluffypangolin.paysville.ui.players_number;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import be.thefluffypangolin.paysville.databinding.FragmentPlayersNumberBinding;

public class PlayersNumberFragment extends Fragment {

    private PlayersNumberViewModel playersNumberViewModel;
    private FragmentPlayersNumberBinding binding;

    private NumberPicker picker;
    private FloatingActionButton fab;

    public static PlayersNumberFragment newInstance() {
        return new PlayersNumberFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        playersNumberViewModel = new ViewModelProvider(this).get(PlayersNumberViewModel.class);
        binding = FragmentPlayersNumberBinding.inflate(inflater, container, false);
        picker = binding.numberPickerPlayersNumber;
        fab = binding.fabPlayersNumber;

        playersNumberViewModel.getNumberOfPlayers().observe(getViewLifecycleOwner(), picker::setValue);

        picker.setMinValue(2);
        picker.setMaxValue(10);
        picker.setOnValueChangedListener
                ((picker, oldVal, newVal) -> playersNumberViewModel.getNumberOfPlayers().setValue(newVal));

        fab.setOnClickListener(v -> {
            // naviguer au fragment suivant
            int numberOfPlayers = picker.getValue();
            PlayersNumberFragmentDirections.ActionPlayersNumberToNames action
                    = PlayersNumberFragmentDirections.actionPlayersNumberToNames(numberOfPlayers);
            Navigation.findNavController(v).navigate(action);
        });

        return binding.getRoot();
    }
}