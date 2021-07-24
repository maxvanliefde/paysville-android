package be.thefluffypangolin.paysville.ui.game_points;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import be.thefluffypangolin.paysville.GameActivity;
import be.thefluffypangolin.paysville.GameViewModel;
import be.thefluffypangolin.paysville.R;
import be.thefluffypangolin.paysville.model.PaysVilleGame;

public class VerifyPointsDialogFragment extends DialogFragment {

    private GameActivity activity;
    private PaysVilleGame game;

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

        builder.setTitle("Un petit point sur les scores !")
                .setMessage(game.getPointsEvolution())
                .setPositiveButton("OK", this::buttonsListener)
                .setNegativeButton("Modifier", this::buttonsListener);

        return builder.create();
    }

    private void buttonsListener(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                // enregistre les points
                game.updateCurrentPoints();

                // vérifie si la partie est finie, sinon relance une manche
                NavDirections action;
                if (game.isGameFinished()) {
                    action = VerifyPointsDialogFragmentDirections.actionVerifyToFinished();
                } else {
                    try {
                        game.addNewRound();
                    } catch (PaysVilleGame.NoLetterLeftException e) {
                        e.printStackTrace();
                    }
                    action = VerifyPointsDialogFragmentDirections.actionVerifyToLaunch();
                }
                Navigation.findNavController(activity, R.id.nav_host_fragment_activity_game).navigate(action);

            case DialogInterface.BUTTON_NEGATIVE:
                dialog.dismiss();
        }
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        activity = (GameActivity) context;
        game = new ViewModelProvider(activity).get(GameViewModel.class).getGame();
    }
}
