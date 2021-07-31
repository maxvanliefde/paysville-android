package be.thefluffypangolin.paysville.ui.game_winners;

import android.content.Context;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import be.thefluffypangolin.paysville.GameActivity;
import be.thefluffypangolin.paysville.GameViewModel;
import be.thefluffypangolin.paysville.R;
import be.thefluffypangolin.paysville.databinding.FragmentGameWinnersBinding;
import be.thefluffypangolin.paysville.model.PaysVilleGame;
import be.thefluffypangolin.paysville.model.Player;

public class WinnersFragment extends Fragment {

    private PaysVilleGame game;
    private GameActivity activity;
    private FragmentGameWinnersBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentGameWinnersBinding.inflate(inflater, container, false);
        ExtendedFloatingActionButton fab = activity.getFAB();
        TextView textView = binding.winnersText;
        LinearLayoutCompat winnersLayout = binding.winnersLayout;
        List<Player> winners = game.getWinners();

        if (winners.size() == 1)
            textView.setText(R.string.winner_is);
        else
            textView.setText(R.string.winners_are);

        for (Player p : winners) {
            TextView textViewName = new TextView(winnersLayout.getContext());
            textViewName.setText(p.getName());
            textViewName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
            textViewName.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            winnersLayout.addView(textViewName);
        }

        fab.setIconResource(R.drawable.ic_check_mark_24);
        fab.setText(R.string.finish);
        fab.setOnClickListener(v -> activity.finish());

        return binding.getRoot();
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        activity = (GameActivity) context;
        game = new ViewModelProvider(activity).get(GameViewModel.class).getGame();
    }
}
