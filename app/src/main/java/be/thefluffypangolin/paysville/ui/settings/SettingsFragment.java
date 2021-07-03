package be.thefluffypangolin.paysville.ui.settings;

import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.EditTextPreference;
import androidx.preference.EditTextPreferenceDialogFragmentCompat;
import androidx.preference.ListPreference;
import androidx.preference.MultiSelectListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SeekBarPreference;
import androidx.preference.SwitchPreferenceCompat;

import org.jetbrains.annotations.NotNull;

import be.thefluffypangolin.paysville.R;
import be.thefluffypangolin.paysville.databinding.FragmentSettingsBinding;

/**
 * Gère la page des paramètres de jeu
 */
public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings_page, rootKey);

        /* Fin de partie */
        ListPreference endGameList = findPreference("end_game_list");
        EditTextPreference endGamePoints = findPreference("end_game_points");

        if (endGamePoints != null && endGameList != null) {
            // n'entrer que des nombres dans endGamePoints
            endGamePoints.setOnBindEditTextListener(editText -> editText.setInputType(InputType.TYPE_CLASS_NUMBER));

            // summary du nombre de points
            endGamePoints.setSummaryProvider((Preference.SummaryProvider<EditTextPreference>) preference -> {
                String text = preference.getText();
                if (endGameList.getValue().equals("all_letters")) return null;
                if (text == null || text.length() == 0) return "Entrez un nombre !";
                return "Le premier joueur à atteindre " + text + " points gagne la partie";
            });

            // listener & check pour afficher le nombre de points uniquement si nécessaire
            endGameList.setOnPreferenceChangeListener((preference, newValue) -> {
                endGamePoints.setEnabled(newValue.equals("fixed_points"));
                return true;
            });
            if (endGameList.getValue().equals("fixed_points"))
                endGamePoints.setEnabled(true);
        }

        /* Fin de manche */
        SwitchPreferenceCompat timerSwitch = findPreference("timer_switch");
        EditTextPreference timerDuration = findPreference("timer_duration");

        if (timerDuration != null && timerSwitch != null) {
            // n'entrer que des nombres
            timerDuration.setOnBindEditTextListener(editText -> editText.setInputType(InputType.TYPE_CLASS_NUMBER));

            // summary du nombre de secondes
            timerDuration.setSummaryProvider((Preference.SummaryProvider<EditTextPreference>) preference -> {
                String text = preference.getText();
                if (!timerSwitch.isChecked()) return null;
                if (text == null || text.length() == 0) return "Entrez une durée !";
                return "Chaque manche dure " + text + " secondes";
            });
        }

        /* Lettres difficiles */
        PreferenceCategory difficultLettersCategory = findPreference("dl_category");
        SwitchPreferenceCompat difficultLettersSwitch = findPreference("dl_switch");
        MultiSelectListPreference difficultLettersList = findPreference("dl_list");
        EditTextPreference difficultLettersTime = findPreference("dl_time");

        if (timerSwitch != null && difficultLettersCategory != null) {
            // masquer ou non la section lettres difficiles
            if (timerSwitch.isChecked()) difficultLettersCategory.setVisible(true);
            timerSwitch.setOnPreferenceChangeListener((preference, newValue) -> {
                difficultLettersCategory.setVisible((Boolean) newValue);
                return true;
            });
        }

        if (difficultLettersSwitch != null && difficultLettersList != null) {
            // summary de la liste des lettres difficiles
            difficultLettersList.setSummaryProvider((Preference.SummaryProvider<MultiSelectListPreference>) preference -> {
                if (!difficultLettersSwitch.isChecked()) return null;
                else if (difficultLettersList.getValues() == null || difficultLettersList.getValues().size() == 0)
                    return "Sélectionnez au moins une lettre !";
                else {
                    StringBuilder text = new StringBuilder();
                    for (int i = 0; i < difficultLettersList.getValues().size(); i++)
                        text.append(difficultLettersList.getValues().toArray()[i]).append(" ");
                    return text.toString();
                }
            });
        }

        if (difficultLettersSwitch != null && difficultLettersTime != null) {
            // n'entrer que des nombres
            difficultLettersTime.setOnBindEditTextListener(editText -> editText.setInputType(InputType.TYPE_CLASS_NUMBER));

            // summary
            difficultLettersTime.setSummaryProvider((Preference.SummaryProvider<EditTextPreference>) preference -> {
                String text = preference.getText();
                if (!difficultLettersSwitch.isChecked()) return null;
                if (text == null || text.length() == 0) return "Entrez une durée !";
                return "Le compte à rebours dure " + text + " secondes de plus pour les lettres difficiles";
            });
        }

        /* Lettre compte double */
        SwitchPreferenceCompat randomDoubleSwitch = findPreference("rd_switch");
        SeekBarPreference randomDoubleProbability = findPreference("rd_probability");

        if (randomDoubleSwitch != null && randomDoubleProbability != null) {
            // seekbar
            randomDoubleProbability.setMax(100);
            randomDoubleProbability.setMin(0);
            randomDoubleProbability.setSeekBarIncrement(1);
            randomDoubleProbability.setUpdatesContinuously(true);

            // summary
            randomDoubleProbability.setSummary
                    (randomDoubleProbability.getValue() + " chances sur " + randomDoubleProbability.getMax());

            randomDoubleProbability.setOnPreferenceChangeListener((preference, newValue) -> {
                if (!randomDoubleSwitch.isChecked()) {
                    randomDoubleProbability.setSummary(null);
                    return true;
                }
                else if (((Integer)newValue) == randomDoubleProbability.getMin()) {
                    randomDoubleProbability.setSummary("0% est trop peu, désactivez plutôt l'option !");
                    return false;
                }
                else if (((Integer)newValue) == randomDoubleProbability.getMax()) {
                    randomDoubleProbability.setSummary("Trop élevé, la probabilité serait de 100% !");
                    return false;
                }
                else {
                    int value = ((Integer) newValue);
                    randomDoubleProbability.setSummary(value + " % de chances");
                    return true;
                }
            });

            randomDoubleSwitch.setOnPreferenceChangeListener((preference, newValue) -> {
                if (!((Boolean) newValue)) randomDoubleProbability.setSummary(null);
                else randomDoubleProbability.setValue(randomDoubleProbability.getValue());
                return true;
            });
        }
    }
}