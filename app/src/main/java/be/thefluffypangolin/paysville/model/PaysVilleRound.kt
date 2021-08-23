package be.thefluffypangolin.paysville.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Cette classe représente une manche d'une partie.
 */
@Parcelize
class PaysVilleRound(
    val letter: Char,
    private val points: MutableMap<Player, Int> = HashMap()
) : Parcelable {
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
    fun doubleScores(players: Array<Player>) {
        for (p in players)
            setScore(p, 2 * getScore(p)!!)
    }
}