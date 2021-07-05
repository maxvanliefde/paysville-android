package be.thefluffypangolin.paysville.ui.players_number;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.thefluffypangolin.paysville.databinding.FragmentPlayersNumberBinding;

public class PlayersNumberFragment extends Fragment {

    private PlayersNumberViewModel playersNumberViewModel;
    private FragmentPlayersNumberBinding binding;

    public static PlayersNumberFragment newInstance() {
        return new PlayersNumberFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        playersNumberViewModel = new ViewModelProvider(this).get(PlayersNumberViewModel.class);
        // TODO: Use the ViewModel

        binding = FragmentPlayersNumberBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}