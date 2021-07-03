package be.thefluffypangolin.paysville.ui.home;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.preference.PreferenceManager;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import be.thefluffypangolin.paysville.model.GameParameters;

/**
 * Gère l'AlertDialog indiquant les paramètres du jeu lors du lancement d'une partie
 */
public class LaunchGameDialogFragment extends DialogFragment {

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface LaunchGameDialogListener {
        void onDialogLaunchClick(DialogFragment dialog);
        void onDialogModifyClick(DialogFragment dialog);
    }

    // listener pour les actions
    LaunchGameDialogListener listener;

    // pour instancier le listener
    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        try {
            listener = (LaunchGameDialogListener) context;
        } catch (ClassCastException activity) {
            // l'activité n'implémente pas l'interface
            throw new ClassCastException(requireActivity().toString() + " must implement LaunchGameDialogListener");
        }
    }

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Avant de démarrer...")
                .setMessage("Voici les paramètres du jeu, il est encore temps de les modifier si vous le souhaitez !\n\n"
                        + getGameParameters().toString())
                .setPositiveButton("Démarrer la partie",
                        (dialog, which) -> listener.onDialogLaunchClick(LaunchGameDialogFragment.this))
                .setNegativeButton("Modifier les paramètres",
                        (dialog, which) -> listener.onDialogModifyClick(LaunchGameDialogFragment.this));
        return builder.create();
    }

    private GameParameters getGameParameters() {
        return new GameParameters(PreferenceManager.getDefaultSharedPreferences(requireContext()));
    }
}
