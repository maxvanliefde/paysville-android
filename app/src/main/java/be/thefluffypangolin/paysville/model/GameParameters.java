package be.thefluffypangolin.paysville.model;

import android.content.SharedPreferences;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

/**
 * La classe GameParameters gère les paramètres d'une partie de Pays-Ville.
 * Les paramètres sont : <br>
 * <ul>
 *     <li>Partie de 26 manches ou à points définis;</li>
 *     <li>Timer ou non pour chaque manche;</li>
 *     <li><ul><li>Timer bonus pour les mauvaises lettres ou non;</li></ul></li>
 *     <li>Probabilité de doubler les points d'une manche;</li>
 */
public class GameParameters {

    public static class BadParameterException extends Exception {
        public BadParameterException(String param) {
            super("Le paramètre suivant n'est pas sur true :" + param);
        }
    }

    public static class GameNotReadyException extends Exception {
        private Set<String> reasons;
        public GameNotReadyException(Set<String> reasons) {
            super();
            this.reasons = reasons;
        }
        public Set<String> getReasons() {
            return reasons;
        }
    }

    private boolean gameEndsWithPoints;
    private int pointsGameEnd;

    private boolean timerOn;
    private int timerDuration;

    private boolean bonusTimerOn;
    private Set<String> bonusTimerLetters;
    private int bonusTimerDuration;

    private boolean randomDoublePointsOn;
    private int probabilityRandom;


    public boolean doGameEndsWithPoints() {
        return gameEndsWithPoints;
    }

    public void setGameEndsWithPoints(boolean gameEndsWithPoints) {
        this.gameEndsWithPoints = gameEndsWithPoints;
    }

    public int getPointsGameEnd() {
        return pointsGameEnd;
    }

    public void setPointsGameEnd(int pointsGameEnd) throws BadParameterException {
        if (!doGameEndsWithPoints()) throw new BadParameterException("pointsGameEnd");
        this.pointsGameEnd = pointsGameEnd;
    }


    public boolean isTimerOn() {
        return timerOn;
    }

    public void setTimerOn(boolean timerOn) {
        this.timerOn = timerOn;
    }

    public int getTimerDuration() {
        return timerDuration;
    }

    public void setTimerDuration(int timerDuration) throws BadParameterException {
        if (!timerOn) throw new BadParameterException("timerOn");
        this.timerDuration = timerDuration;
    }


    public boolean isBonusTimerOn() {
        return bonusTimerOn;
    }

    public void setBonusTimerOn(boolean bonusTimerOn) {
        this.bonusTimerOn = bonusTimerOn;
    }

    public int getBonusTimerDuration() {
        return bonusTimerDuration;
    }

    public void setBonusTimerDuration(int bonusTimerDuration) throws BadParameterException {
        if (!isBonusTimerOn()) throw new BadParameterException("bonusTimerOn");
        this.bonusTimerDuration = bonusTimerDuration;
    }


    public boolean isRandomDoublePointsOn() {
        return randomDoublePointsOn;
    }

    public void setRandomDoublePointsOn(boolean randomDoublePointsOn) {
        this.randomDoublePointsOn = randomDoublePointsOn;
    }

    public int getProbabilityRandom() {
        return probabilityRandom;
    }

    public void setProbabilityRandom(int probabilityRandom) throws BadParameterException {
        if (!isRandomDoublePointsOn()) throw new BadParameterException("timerOn");
        this.probabilityRandom = probabilityRandom;
    }

    /**
     * Crée une instance de GameParameters paramétrée
     * au moyen du SharedPreferences donné.
     */
    public GameParameters(SharedPreferences sharedPreferences) throws GameNotReadyException {
        Set<String> reasons = new HashSet<>();

        gameEndsWithPoints = sharedPreferences.getString("end_game_list", "").equals("fixed_points");
        if (gameEndsWithPoints) {
            try {
                pointsGameEnd = Integer.parseInt(sharedPreferences.getString("end_game_points", "0"));
            } catch (NumberFormatException e) {
                reasons.add("aucun nombre de points n'a été réglé pour finir la partie");
            }
        } else {
            pointsGameEnd = -1;
        }

        timerOn = sharedPreferences.getBoolean("timer_switch", false);
        if (timerOn) {
            try {
                timerDuration = Integer.parseInt(sharedPreferences.getString("timer_duration", "0"));
            } catch (NumberFormatException e) {
                reasons.add("aucune durée n'a été indiquée pour le compte à rebours");
            }

            bonusTimerOn = sharedPreferences.getBoolean("dl_switch", false);
            if (bonusTimerOn) {
                bonusTimerLetters = sharedPreferences.getStringSet("dl_list", null);
                if (bonusTimerLetters == null || bonusTimerLetters.isEmpty())
                    reasons.add("aucune lettre difficile n'a été indiquée");
                try {
                    bonusTimerDuration = Integer.parseInt(sharedPreferences.getString("dl_time", "0"));
                } catch (NumberFormatException e) {
                    reasons.add("aucune durée n'a été indiquée pour le compte à rebours bonus");
                }
            } else {
                bonusTimerLetters = null;
                bonusTimerDuration = -1;
            }
        } else {
            timerDuration = -1;
            bonusTimerOn = false;
            bonusTimerLetters = null;
            bonusTimerDuration = -1;
        }

        randomDoublePointsOn = sharedPreferences.getBoolean("rd_switch", false);
        if (randomDoublePointsOn) {
            probabilityRandom = sharedPreferences.getInt("rd_probability", 1);
            if (probabilityRandom == 0)
                reasons.add("la probabilité indiquée est de 0%");
            if (probabilityRandom == 100)
                reasons.add("la probabilité indiquée est de 100%");
        } else {
            probabilityRandom = -1;
        }

        if (!reasons.isEmpty()) throw new GameNotReadyException(reasons);
    }

    @NotNull
    @Override
    public String toString() {
        return "GameParameters{" +
                "gameEndsWithPoints=" + gameEndsWithPoints +
                ", pointsGameEnd=" + pointsGameEnd +
                ", timerOn=" + timerOn +
                ", timerDuration=" + timerDuration +
                ", bonusTimerOn=" + bonusTimerOn +
                ", bonusTimerLetters=" + bonusTimerLetters +
                ", bonusTimerDuration=" + bonusTimerDuration +
                ", randomDoublePointsOn=" + randomDoublePointsOn +
                ", probabilityRandom=" + probabilityRandom +
                '}';
    }
}
