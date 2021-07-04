package be.thefluffypangolin.paysville.model;

import android.content.SharedPreferences;

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
    private int probabilityPercentage;

    private boolean isSoundOn;


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

    public Set<String> getBonusTimerLetters() {
        return bonusTimerLetters;
    }

    public void setBonusTimerLetters(Set<String> bonusTimerLetters) {
        this.bonusTimerLetters = bonusTimerLetters;
    }


    public boolean isRandomDoublePointsOn() {
        return randomDoublePointsOn;
    }

    public void setRandomDoublePointsOn(boolean randomDoublePointsOn) {
        this.randomDoublePointsOn = randomDoublePointsOn;
    }

    public int getProbabilityPercentage() {
        return probabilityPercentage;
    }

    public void setProbabilityPercentage(int probabilityPercentage) throws BadParameterException {
        if (!isRandomDoublePointsOn()) throw new BadParameterException("timerOn");
        this.probabilityPercentage = probabilityPercentage;
    }


    public boolean isSoundOn() {
        return isSoundOn;
    }

    public void setSoundOn(boolean soundOn) {
        isSoundOn = soundOn;
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
                pointsGameEnd = Integer.parseInt(sharedPreferences.getString("end_game_points", ""));
            } catch (NumberFormatException e) {
                reasons.add("aucun nombre de points n'a été réglé pour finir la partie");
            }
        } else {
            pointsGameEnd = -1;
        }

        timerOn = sharedPreferences.getBoolean("timer_switch", false);
        if (timerOn) {
            try {
                timerDuration = Integer.parseInt(sharedPreferences.getString("timer_duration", ""));
            } catch (NumberFormatException e) {
                reasons.add("aucune durée n'a été indiquée pour le compte à rebours");
            }

            bonusTimerOn = sharedPreferences.getBoolean("dl_switch", false);
            if (bonusTimerOn) {
                bonusTimerLetters = sharedPreferences.getStringSet("dl_list", null);
                if (bonusTimerLetters == null || bonusTimerLetters.isEmpty())
                    reasons.add("aucune lettre difficile n'a été indiquée");
                try {
                    bonusTimerDuration = Integer.parseInt(sharedPreferences.getString("dl_time", ""));
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
            probabilityPercentage = sharedPreferences.getInt("rd_probability", 0);
            if (probabilityPercentage == 0)
                reasons.add("la probabilité indiquée est de 0%");
            if (probabilityPercentage == 100)
                reasons.add("la probabilité indiquée est de 100%");
        } else {
            probabilityPercentage = -1;
        }

        isSoundOn = sharedPreferences.getBoolean("sound_switch", false);

        if (!reasons.isEmpty()) throw new GameNotReadyException(reasons);
    }

    @NotNull
    @Override
    public String toString() {
        StringBuilder msg = new StringBuilder();

        // fin de jeu
        if (doGameEndsWithPoints()) {
            msg.append("* La partie s'arrêtera lorsqu'un joueur aura atteint ")
                    .append(getPointsGameEnd())
                    .append(" points\n");
        } else {
            msg.append("* La partie s'arrêtera lorsque les 26 lettres de l'alphabet auront été jouées\n");
        }

        // timer
        if (isTimerOn()) {
            msg.append("* Compte à rebours activé, chaque manche durera ")
                    .append(getTimerDuration())
                    .append(" secondes\n");

            //timer bonus
            if (isBonusTimerOn()) {
                msg.append("* Une durée de ")
                        .append(getBonusTimerDuration())
                        .append(" secondes sera ajoutée pour les lettres ")
                        .append(getBonusTimerLetters())
                        .append("\n");
            } else {
                msg.append("* Pas de temps supplémentaire pour les lettres difficiles\n");
            }
        } else {
            msg.append("* Compte à rebours désactivé\n");
        }

        // compte double
        if (isRandomDoublePointsOn()) {
            msg.append("* Chaque manche aura une probabilité de ")
                    .append(getProbabilityPercentage())
                    .append("% de compter double pour chaque joueur\n");
        } else {
            msg.append("* Pas d'aléatoire ajouté\n");
        }

        // son
        if (isSoundOn())
            msg.append("* Sons activés");
        else
            msg.append("* Sons désactivés");

        return msg.toString();
    }
}
