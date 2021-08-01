package be.thefluffypangolin.paysville.ui.main_fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import be.thefluffypangolin.paysville.databinding.FragmentMainAboutBinding;

public class AboutFragment extends Fragment {

    private AboutViewModel aboutViewModel;
    private FragmentMainAboutBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        aboutViewModel =
                new ViewModelProvider(this).get(AboutViewModel.class);

        binding = FragmentMainAboutBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textAbout;
        aboutViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}