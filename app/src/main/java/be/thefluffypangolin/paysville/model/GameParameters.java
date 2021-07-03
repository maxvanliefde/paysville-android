package be.thefluffypangolin.paysville.model;

import android.content.SharedPreferences;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import org.jetbrains.annotations.NotNull;

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

    private boolean gameEndsWithPoints;
    private int pointsGameEnd;

    private boolean timerOn;
    private int timerDuration;

    private boolean bonusTimerOn;
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
     * Retourne une instance de GameParameters paramétrée
     * au moyen du SharedPreferences donné.
     */
    public GameParameters(SharedPreferences sharedPreferences) {
        gameEndsWithPoints = sharedPreferences.getString("end_game_list", "").equals("fixed_points");
        if (gameEndsWithPoints)
            pointsGameEnd = Integer.parseInt(sharedPreferences.getString("end_game_points", "0"));

        timerOn = sharedPreferences.getBoolean("timer_switch", false);
        if (timerOn) {
            timerDuration = Integer.parseInt(sharedPreferences.getString("timer_duration", "0"));

            bonusTimerOn = sharedPreferences.getBoolean("dl_switch", false);
            if (bonusTimerOn)
                bonusTimerDuration = Integer.parseInt(sharedPreferences.getString("dl_time", "0"));
        }

        randomDoublePointsOn = sharedPreferences.getBoolean("rd_switch", false);
        if (randomDoublePointsOn)
            probabilityRandom = sharedPreferences.getInt("rd_probability", 20) / 20;
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
                ", bonusTimerDuration=" + bonusTimerDuration +
                ", randomDoublePointsOn=" + randomDoublePointsOn +
                ", probabilityRandom=" + probabilityRandom +
                '}';
    }
}
