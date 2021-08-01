package be.thefluffypangolin.paysville.ui.main_settings;

import android.os.Bundle;
import android.text.InputType;

import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.MultiSelectListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SeekBarPreference;
import androidx.preference.SwitchPreferenceCompat;

import java.util.TreeSet;

import be.thefluffypangolin.paysville.R;

/**
 * Gère la page des paramètres de jeu
 */
public class SettingsFragment extends PreferenceFragmentCompat {

    // Fin de partie
    ListPreference endGameList;
    EditTextPreference endGamePoints;

    // Fin de manche
    SwitchPreferenceCompat timerSwitch;
    EditTextPreference timerDuration;

    // Lettres difficiles
    PreferenceCategory difficultLettersCategory;
    SwitchPreferenceCompat difficultLettersSwitch;
    MultiSelectListPreference difficultLettersList;
    EditTextPreference difficultLettersTime;

    // Lettre compte double
    SwitchPreferenceCompat randomDoubleSwitch;
    SeekBarPreference randomDoubleProbability;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings_page, rootKey);

        /* Fin de partie */
        endGameList = findPreference("end_game_list");
        endGamePoints = findPreference("end_game_points");

        if (endGamePoints != null && endGameList != null) {
            // n'entrer que des nombres dans endGamePoints et placer le curseur à la fin
            endGamePoints.setOnBindEditTextListener(editText -> {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                editText.post(() -> editText.setSelection(editText.getText().length()));
            });

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
        timerSwitch = findPreference("timer_switch");
        timerDuration = findPreference("timer_duration");

        if (timerDuration != null && timerSwitch != null) {
            // n'entrer que des nombres et placer le curseur à la fin
            timerDuration.setOnBindEditTextListener(editText -> {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                editText.post(() -> editText.setSelection(editText.getText().length()));
            });

            // summary du nombre de secondes
            timerDuration.setSummaryProvider((Preference.SummaryProvider<EditTextPreference>) preference -> {
                String text = preference.getText();
                if (!timerSwitch.isChecked()) return null;
                if (text == null || text.length() == 0) return "Entrez une durée !";
                return "Chaque manche dure " + text + " secondes";
            });
        }

        /* Lettres difficiles */
        difficultLettersCategory = findPreference("dl_category");
        difficultLettersSwitch = findPreference("dl_switch");
        difficultLettersList = findPreference("dl_list");
        difficultLettersTime = findPreference("dl_time");

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
                    TreeSet<String> set = new TreeSet<>(difficultLettersList.getValues());
                    for (String s : set) text.append(s).append(" ");
                    return text.toString();
                }
            });
        }

        if (difficultLettersSwitch != null && difficultLettersTime != null) {
            // n'entrer que des nombres et placer le curseur à la fin
            difficultLettersTime.setOnBindEditTextListener(editText -> {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                editText.post(() -> editText.setSelection(editText.getText().length()));
            });

            // summary
            difficultLettersTime.setSummaryProvider((Preference.SummaryProvider<EditTextPreference>) preference -> {
                String text = preference.getText();
                if (!difficultLettersSwitch.isChecked()) return null;
                if (text == null || text.length() == 0) return "Entrez une durée !";
                return "Le compte à rebours dure " + text + " secondes de plus pour les lettres difficiles";
            });
        }

        /* Lettre compte double */
        randomDoubleSwitch = findPreference("rd_switch");
        randomDoubleProbability = findPreference("rd_probability");

        if (randomDoubleSwitch != null && randomDoubleProbability != null) {
            // seekbar
            randomDoubleProbability.setMax(100);
            randomDoubleProbability.setMin(0);
            randomDoubleProbability.setSeekBarIncrement(1);
            randomDoubleProbability.setUpdatesContinuously(true);

            // summary
            if (!randomDoubleSwitch.isChecked())
                randomDoubleProbability.setSummary(null);
            else
                setProbabilitySummary(randomDoubleProbability.getValue());

            randomDoubleProbability.setOnPreferenceChangeListener((preference, newValue) -> {
                setProbabilitySummary((Integer) newValue);
                return true;
            });

            randomDoubleSwitch.setOnPreferenceChangeListener((preference, newValue) -> {
                if (!((Boolean) newValue)) randomDoubleProbability.setSummary(null);
                else setProbabilitySummary(randomDoubleProbability.getValue());
                return true;
            });
        }
    }

    private void setProbabilitySummary(int newValue) {
        if (newValue == randomDoubleProbability.getMin()) {
            randomDoubleProbability.setSummary("0% est trop peu, désactivez plutôt l'option !");
        }
        else if (newValue == randomDoubleProbability.getMax()) {
            randomDoubleProbability.setSummary("Trop élevé, la probabilité serait de 100% !");
        }
        else {
            randomDoubleProbability.setSummary(newValue + " % de chances");
        }
    }
}