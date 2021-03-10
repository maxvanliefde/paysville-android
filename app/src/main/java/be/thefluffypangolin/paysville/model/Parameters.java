package be.thefluffypangolin.paysville.model;

/**
 * La classe Parameters gère les paramètres d'une partie de Pays-Ville.
 * Les paramètres sont : <br>
 * <ul>
 *     <li>Partie de 26 manches ou à points définis;</li>
 *     <li>Timer ou non pour chaque manche;</li>
 *     <li><ul><li>Timer bonus pour les mauvaises lettres ou non;</li></ul></li>
 *     <li>Probabilité de doubler les points d'une manche;</li>
 */
public class Parameters {

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
}
