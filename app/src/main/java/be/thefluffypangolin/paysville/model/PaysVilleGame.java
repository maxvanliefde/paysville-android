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
    private final Map<Player, Integer> actualPoints;

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
        this.actualPoints = new HashMap<>(numberOfPlayers + 1, 1);
        for (Player p : this.players) this.actualPoints.put(p, 0);
    }

    /**
     * Exception lancée lorsqu'une Factory veut générer une nouvelle manche,
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
    }

    /**
     * Génère des manches comprenant une lettre aléatoire chacune.
     */
    public class PaysVilleRoundFactory {
        private final List<Character> letters;
        private int actualIndex;

        /**
         * Crée une nouvelle Factory, avec un alphabet en ordre aléatoire
         */
        public PaysVilleRoundFactory() {
            letters = IntStream.rangeClosed('A', 'Z')
                    .mapToObj(c -> (char) c)
                    .collect(Collectors.toList());
            Collections.shuffle(letters);
            actualIndex = 0;
        }

        /**
         * Crée une nouvelle manche
         * @return une nouvelle manche avec une lettre aléatoire associée,
         * choisie parmi celles restantes
         * @throws NoLetterLeftException si toutes les lettres ont déjà été générées précédemment
         */
        public PaysVilleRound newRound() throws NoLetterLeftException {
            try {
                return new PaysVilleRound(letters.get(actualIndex++));
            } catch (IndexOutOfBoundsException e) {
                throw new NoLetterLeftException();
            }
        }
    }
}
