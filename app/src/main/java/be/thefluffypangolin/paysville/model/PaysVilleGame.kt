package be.thefluffypangolin.paysville.model

import java.lang.Exception
import java.lang.IndexOutOfBoundsException
import java.lang.StringBuilder
import java.util.*
import kotlin.collections.ArrayList

/**
 * Cette classe représente une partie de Pays-Ville.
 */
class PaysVilleGame(
    val parameters: GameParameters,
    val numberOfPlayers: Int,
    val players: Array<Player>
) {
    private val factory = PaysVilleRoundFactory()
    private val rounds = ArrayList<PaysVilleRound>()
    private val currentPoints: MutableMap<Player, Int> = HashMap(numberOfPlayers + 1, 1F)

    init {
        for (p in players) currentPoints[p] = 0
    }

    val currentRound: PaysVilleRound
        get() = rounds[rounds.size - 1]
    val currentRoundNumber: Int
        get() = rounds.size

    /**
     * Exception lancée lorsqu'une nouvelle manche veut être créée,
     * mais que toutes les lettres de l'alphabet ont déjà été générées
     */
    class NoLetterLeftException :
        Exception("Toutes les lettres ont déjà été générées")

    /**
     * Cette classe représente une manche d'une partie.
     */
    inner class PaysVilleRound(val letter: Char) {
        private val points = HashMap<Player, Int>(numberOfPlayers + 1, 1F)

        /**
         * @return la durée du timer en secondes pour cette manche, ou 0 s'il est désactivé
         */
        val timerDuration: Int
            get() =
                if (!parameters.isTimerOn) 0
                else {
                    val time = parameters.timerDuration
                    if (parameters.isBonusTimerOn
                        && parameters.bonusTimerLetters!!.contains(letter.toString())
                    )
                        time + parameters.bonusTimerDuration
                    else time
                }

        /**
         * Met à jour le score obtenu par un joueur
         */
        fun setScore(player: Player, score: Int) {
            points[player] = score
        }

        /**
         * @return le score obtenu par le joueur, ou null s'il n'a pas de score associé
         */
        fun getScore(player: Player): Int? {
            return points[player]
        }

        /**
         * Double les points encodés de chaque joueur.
         */
        fun doubleScores() {
            for (i in 0 until numberOfPlayers)
                setScore(players[i], 2 * getScore(players[i])!!)
        }
    }

    /**
     * Génère des manches comprenant une lettre aléatoire chacune.
     */
    inner class PaysVilleRoundFactory {
        private val letters: List<Char> = ('A'..'Z').toMutableList().apply { shuffle() }
        private var currentIndex: Int = 0

        /**
         * Crée une nouvelle manche
         * @return une nouvelle manche avec une lettre aléatoire associée,
         * choisie parmi celles restantes
         */
        fun newRound(): PaysVilleRound {
            try {
                return PaysVilleRound(letters[currentIndex++])
            } catch (e: IndexOutOfBoundsException) {
                throw NoLetterLeftException()
            }
        }
    }

    /**
     * Ajoute une nouvelle manche à la liste des manches
     * @return la lettre de la nouvelle manche
     * @throws NoLetterLeftException si plus aucune lettre de l'alphabet n'est disponible
     */
    fun addNewRound(): Char {
        val round = factory.newRound()
        rounds.add(round)
        return round.letter
    }

    /**
     * Ajoute les scores de chaque joueur à la manche actuelle,
     * sans mettre à jour les points totaux de la partie.
     * Le joueur correspondant à scores\[i] doit être players\[i].
     * @param scores les scores obtenus par les joueurs [.players] à la manche actuelle
     */
    fun addAllScoresToCurrentRound(scores: IntArray) {
        for (i in 0 until numberOfPlayers) {
            currentRound.setScore(players[i], scores[i])
        }
    }

    /**
     * Met à jour les points totaux de la partie en rajoutant ceux de la manche actuelle.
     */
    fun updateCurrentPoints() {
        for (i in 0 until numberOfPlayers) {
            val currentScore = currentPoints[players[i]]!!
            val newScore = currentRound.getScore(players[i])!!
            currentPoints[players[i]] = currentScore + newScore
        }
    }

    /**
     * Vérifie si la partie est finie, en fonction des paramètres du jeu.
     */
    val isGameFinished: Boolean
        get() {
            if (currentRoundNumber == 26) return true
            else if (parameters.gameEndsWithPoints) {
                for (p in players) {
                    val points = currentPoints[p]!!
                    if (points >= parameters.pointsGameEnd)
                        return true
                }
                return false
            } else return false
        }


    /**
     * Récupère le ou les gagnant.e.s de la partie
     * @return null si la partie n'est pas terminée,
     * ou une liste contenant le ou les gagnant.e.s de la partie sinon
     */
    val winners: List<Player>?
        get() =
            if (!isGameFinished) null
            else {
                var max = 0
                val winners = ArrayList<Player>()
                for (i in 0 until numberOfPlayers) {
                    val score = currentPoints[players[i]]!!
                    if (score > max) {
                        max = score
                        winners.clear()
                        winners.add(players[i])
                    } else if (score == max) {
                        winners.add(players[i])
                    }
                }
                winners
            }

    val pointsEvolution: String
        get() {
            val s = StringBuilder()
            for (i in 0 until numberOfPlayers) {
                val player = players[i]
                val actualScore = currentPoints[player]!!
                val newScore = currentRound.getScore(player)!!
                s.append(player.name).append(" :\t")
                    .append(actualScore).append(" + ").append(newScore)
                    .append(" => ").append(actualScore + newScore)
                    .append("\n")
            }
            return s.toString()
        }
}