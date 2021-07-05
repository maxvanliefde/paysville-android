package be.thefluffypangolin.paysville.ui.players_names;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.thefluffypangolin.paysville.databinding.FragmentPlayersNamesBinding;

public class PlayersNamesFragment extends Fragment {

    private PlayersNamesViewModel playersNamesViewModel;
    private FragmentPlayersNamesBinding binding;

    public static PlayersNamesFragment newInstance() {
        return new PlayersNamesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        playersNamesViewModel = new ViewModelProvider(this).get(PlayersNamesViewModel.class);
        // TODO: Use the ViewModel

        binding = FragmentPlayersNamesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}