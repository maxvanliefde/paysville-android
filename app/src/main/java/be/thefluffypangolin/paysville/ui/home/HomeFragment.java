package be.thefluffypangolin.paysville.ui.home;

import android.app.Application;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.preference.PreferenceManager;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import be.thefluffypangolin.paysville.R;
import be.thefluffypangolin.paysville.databinding.FragmentHomeBinding;
import be.thefluffypangolin.paysville.model.GameParameters;
import be.thefluffypangolin.paysville.ui.settings.SettingsFragment;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
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