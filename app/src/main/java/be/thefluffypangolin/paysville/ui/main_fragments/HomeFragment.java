package be.thefluffypangolin.paysville.ui.main_fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import be.thefluffypangolin.paysville.databinding.FragmentMainHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentMainHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMainHomeBinding.inflate(inflater, container, false);
        ExtendedFloatingActionButton extendedFab = binding.launchGameButton;
        extendedFab.setOnClickListener(this::showConfirmationDialog);
        return binding.getRoot();
    }

    private void showConfirmationDialog(View v) {
        NavDirections action = HomeFragmentDirections.actionHomeToLaunchDialog();
        Navigation.findNavController(v).navigate(action);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}