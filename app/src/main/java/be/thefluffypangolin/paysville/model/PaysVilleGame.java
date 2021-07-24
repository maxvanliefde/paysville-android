package be.thefluffypangolin.paysville.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Cette classe représente une partie de Pays-Ville.
 */
public class PaysVilleGame {
    private final GameParameters parameters;
    private final int numberOfPlayers;
    private final Player[] players;
    private final PaysVilleRoundFactory factory;
    private final List<PaysVilleRound> rounds;
    private final Map<Player, Integer> currentPoints;

    /**
     * Crée une nouvelle partie de Pays-Ville.
     * Les paramètres, le nombre de joueurs et la liste des joueurs sont donnés.
     * @param parameters les paramètres voulus pour le jeu
     * @param numberOfPlayers le nombre de joueurs
     * @param players la liste des joueurs
     *                (voir aussi {@link GameParameters#generatePlayersList(String[])}
     */
    public PaysVilleGame(GameParameters parameters, int numberOfPlayers, Player[] players) {
        this.parameters = parameters;
        this.numberOfPlayers = numberOfPlayers;
        this.players = players;

        this.factory = new PaysVilleRoundFactory();
        this.rounds = new ArrayList<>();
        this.currentPoints = new HashMap<>(numberOfPlayers + 1, 1);
        for (Player p : this.players) this.currentPoints.put(p, 0);
    }

    /**
     * Exception lancée lorsqu'une nouvelle manche veut être créée,
     * mais que toutes les lettres de l'alphabet ont déjà été générées
     */
    public static class NoLetterLeftException extends Exception {
        public NoLetterLeftException() {
            super("Toutes les lettres ont déjà été générées");
        }
    }

    /**
     * Cette classe représente une manche d'une partie.
     */
    public class PaysVilleRound {
        private final char letter;
        private final Map<Player, Integer> points;

        /**
         * Initialise une manche avec la lettre donnée
         */
        public PaysVilleRound(char letter) {
            this.letter = letter;
            this.points = new HashMap<>(numberOfPlayers+1, 1);
        }

        /**
         * @return la lettre de la manche
         */
        public char getLetter() {
            return letter;
        }

        /**
         * Met à jour le score obtenu par un joueur
         * @param player un joueur
         * @param score son score
         * @return le score précédent associé au joueur, ou null s'il n'en avait pas encore
         */
        public Integer setScore(Player player, int score) {
            return this.points.put(player, score);
        }

        /**
         * @param player un joueur
         * @return le score obtenu par le joueur, ou null s'il n'a pas de score associé
         */
        public Integer getScore(Player player) {
            return this.points.get(player);
        }

        /**
         * @return la durée du timer en secondes pour cette manche, ou 0 s'il est désactivé
         */
        public int getTimer() {
            if (!parameters.isTimerOn())
                return 0;
            else {
                int time = parameters.getTimerDuration();
                if (parameters.isBonusTimerOn()
                        && parameters.getBonusTimerLetters().contains(Character.toString(letter)))
                    return time + parameters.getBonusTimerDuration();
                else
                    return time;
            }
        }
    }

    /**
     * Génère des manches comprenant une lettre aléatoire chacune.
     */
    public class PaysVilleRoundFactory {
        private final List<Character> letters;
        private int currentIndex;

        /**
         * Crée une nouvelle Factory, avec un alphabet en ordre aléatoire
         */
        public PaysVilleRoundFactory() {
            letters = IntStream.rangeClosed('A', 'Z')
                    .mapToObj(c -> (char) c)
                    .collect(Collectors.toList());
            Collections.shuffle(letters);
            currentIndex = 0;
        }

        /**
         * Crée une nouvelle manche
         * @return une nouvelle manche avec une lettre aléatoire associée,
         * choisie parmi celles restantes
         * @throws NoLetterLeftException si toutes les lettres ont déjà été générées précédemment
         */
        public PaysVilleRound newRound() throws NoLetterLeftException {
            try {
                return new PaysVilleRound(letters.get(currentIndex++));
            } catch (IndexOutOfBoundsException e) {
                throw new NoLetterLeftException();
            }
        }
    }

    /**
     * @return les paramètres de la partie
     */
    public GameParameters getParameters() {
        return parameters;
    }

    /**
     * @return la liste des manches de la partie
     */
    public List<PaysVilleRound> getRounds() {
        return rounds;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public Player[] getPlayers() {
        return players;
    }

    /**
     * @return les points actuels, sous la forme d'un mappage entre un joueur et ses points
     */
    public Map<Player, Integer> getCurrentPoints() {
        return currentPoints;
    }

    /**
     * @return la manche actuelle, i.e. la dernière de la liste rounds
     */
    public PaysVilleRound getCurrentRound() {
        return rounds.get(rounds.size() - 1);
    }

    /**
     * Ajoute une nouvelle manche à la liste des manches
     * @return la lettre de la nouvelle manche
     * @throws NoLetterLeftException si plus aucune lettre de l'alphabet n'est disponible
     */
    public char addNewRound() throws NoLetterLeftException {
        PaysVilleRound round = factory.newRound();
        rounds.add(round);
        return round.getLetter();
    }

    /**
     * Retourne le numéro de la manche actuelle, i.e. la dernière ajoutée avec {@link #addNewRound()},
     * ce qui correspond à la longueur de la liste rounds.
     * @return le numéro de la dernière manche ajoutée
     */
    public int getCurrentRoundNumber() {
        return rounds.size();
    }

    /**
     * Ajoute le score d'un joueur à la manche actuelle,
     * sans mettre à jour les points totaux de la partie.
     * @param player un joueur
     * @param score son score à la manche actuelle
     */
    public void addScoreToCurrentRound(Player player, int score) {
        getCurrentRound().setScore(player, score);
    }

    /**
     * Ajoute les scores de chaque joueur à la manche actuelle,
     * sans mettre à jour les points totaux de la partie.
     * Le joueur correspondant à scores[i] doit être players[i].
     * @param scores les scores obtenus par les joueurs {@link #players} à la manche actuelle
     */
    public void addAllScoresToCurrentRound(int[] scores) {
        for (int i = 0; i < numberOfPlayers; i++) {
            addScoreToCurrentRound(players[i], scores[i]);
        }
    }

    /**
     * Met à jour les points totaux de la partie en rajoutant ceux de la manche actuelle.
     */
    public void updateCurrentPoints() {
        for (int i = 0; i < numberOfPlayers; i++) {
            Integer currentPoints = getCurrentPoints().get(players[i]);
            Integer newScore = getCurrentRound().getScore(players[i]);
            if (currentPoints != null && newScore != null)
                getCurrentPoints().put(players[i], currentPoints + newScore);
        }
    }

    /**
     * Vérifie si la partie est finie, en fonction des paramètres du jeu.
     * @return vrai si le jeu est terminé, ou faux sinon
     */
    public boolean isGameFinished() {
        if (parameters.doGameEndsWithPoints()) {
            for (Player p : players) {
                Integer points = getCurrentPoints().get(p);
                if (points != null && points >= parameters.getPointsGameEnd()) {
                    return true;
                }
            }
            return false;
        } else {
            return getCurrentRoundNumber() == 26;
        }
    }

    /**
     * todo vérifier s'il y a plusieurs gagants
     * @return le gagnant de la partie, ou null si elle n'est pas terminée
     */
    public Player getWinner() {
        if (!isGameFinished()) return null;
        else {
            int max = 0;
            Player winner = players[0];
            for (int i = 0; i < numberOfPlayers; i++) {
                Integer score = getCurrentPoints().get(players[i]);
                if (score != null && score > max)
                    winner = players[i];
            }
            return winner;
        }
    }

    public String getPointsEvolution() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < numberOfPlayers; i++) {
            Player player = players[i];
            int actualScore = getCurrentPoints().get(player);
            int newScore = getCurrentRound().getScore(player);
            s.append(player.getName()).append(" :\t")
                    .append(actualScore).append(" + ").append(newScore)
                    .append(" => ").append(actualScore + newScore)
                    .append("\n");
        }
        return s.toString();
    }
}
