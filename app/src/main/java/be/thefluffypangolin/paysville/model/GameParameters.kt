package be.thefluffypangolin.paysville.model

import android.os.Parcelable
import android.content.SharedPreferences
import be.thefluffypangolin.paysville.model.GameParameters.GameNotReadyException
import android.os.Parcel
import android.os.Parcelable.Creator
import be.thefluffypangolin.paysville.model.GameParameters
import be.thefluffypangolin.paysville.model.Player
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.Parceler
import java.lang.Exception
import java.lang.NumberFormatException
import java.lang.StringBuilder
import java.util.*

/**
 * La classe GameParameters gère les paramètres d'une partie de Pays-Ville.
 * Les paramètres sont : <br></br>
 *
 *  * Partie de 26 manches ou à points définis;
 *  * Timer ou non pour chaque manche;
 *  *  * Timer bonus pour les mauvaises lettres ou non;
 *  * Probabilité de doubler les points d'une manche;
 *  * Sons du jeu activés ou non
 *
 */
@Parcelize
class GameParameters(
    @get:JvmName("doGameEndsWithPoints")
    var gameEndsWithPoints: Boolean = false,
    var pointsGameEnd: Int = 0,
    var isTimerOn: Boolean = false,
    var timerDuration: Int = 0,
    var isBonusTimerOn: Boolean = false,
    var bonusTimerLetters: Set<String?>? = null,
    var bonusTimerDuration: Int = 0,
    var isRandomDoublePointsOn: Boolean = false,
    var probabilityPercentage: Int = 0,
    var isSoundOn: Boolean = false
) : Parcelable {

    class GameNotReadyException(val reasons: Set<String>) : Exception()

    /**
     * Crée une instance de GameParameters paramétrée
     * au moyen du SharedPreferences donné.
     */
    constructor(sharedPreferences: SharedPreferences) : this() {
        val reasons: MutableSet<String> = HashSet()
        gameEndsWithPoints = sharedPreferences.getString("end_game_list", "") == "fixed_points"
        if (gameEndsWithPoints) {
            try {
                pointsGameEnd = sharedPreferences.getString("end_game_points", "")!!.toInt()
            } catch (e: NumberFormatException) {
                reasons.add("aucun nombre de points n'a été réglé pour finir la partie")
            }
        } else {
            pointsGameEnd = -1
        }
        isTimerOn = sharedPreferences.getBoolean("timer_switch", false)
        if (isTimerOn) {
            try {
                timerDuration = sharedPreferences.getString("timer_duration", "")!!.toInt()
            } catch (e: NumberFormatException) {
                reasons.add("aucune durée n'a été indiquée pour le compte à rebours")
            }
            isBonusTimerOn = sharedPreferences.getBoolean("dl_switch", false)
            if (isBonusTimerOn) {
                bonusTimerLetters = sharedPreferences.getStringSet("dl_list", null)
                if (bonusTimerLetters == null || bonusTimerLetters!!.isEmpty()) reasons.add("aucune lettre difficile n'a été indiquée")
                try {
                    bonusTimerDuration = sharedPreferences.getString("dl_time", "")!!.toInt()
                } catch (e: NumberFormatException) {
                    reasons.add("aucune durée n'a été indiquée pour le compte à rebours bonus")
                }
            } else {
                bonusTimerLetters = null
                bonusTimerDuration = -1
            }
        } else {
            timerDuration = -1
            isBonusTimerOn = false
            bonusTimerLetters = null
            bonusTimerDuration = -1
        }
        isRandomDoublePointsOn = sharedPreferences.getBoolean("rd_switch", false)
        if (isRandomDoublePointsOn) {
            probabilityPercentage = sharedPreferences.getInt("rd_probability", 0)
            if (probabilityPercentage == 0) reasons.add("la probabilité indiquée est de 0%")
            if (probabilityPercentage == 100) reasons.add("la probabilité indiquée est de 100%")
        } else {
            probabilityPercentage = -1
        }
        isSoundOn = sharedPreferences.getBoolean("sound_switch", false)
        if (reasons.isNotEmpty()) throw GameNotReadyException(reasons)
    }

    /**
     * Génère un booléen avec la probabilité de l'instance.
     * @return faux si [.randomDoublePointsOn] est faux <br></br>
     * vrai ou faux avec une probabilité de [.probabilityPercentage]% de vrai sinon
     */
    fun randomBoolean(): Boolean {
        return if (!isRandomDoublePointsOn) false else Math.random() <= probabilityPercentage / 100.0
    }

    override fun toString(): String {
        val msg = StringBuilder()

        // fin de jeu
        if (gameEndsWithPoints) {
            msg.append("* La partie s'arrêtera lorsqu'un joueur aura atteint ")
                .append(pointsGameEnd)
                .append(" points\n")
        } else {
            msg.append("* La partie s'arrêtera lorsque les 26 lettres de l'alphabet auront été jouées\n")
        }

        // timer
        if (isTimerOn) {
            msg.append("* Compte à rebours activé, chaque manche durera ")
                .append(timerDuration)
                .append(" secondes\n")

            //timer bonus
            if (isBonusTimerOn) {
                val letters = StringBuilder(
                    TreeSet(
                        bonusTimerLetters
                    ).toString()
                )
                letters.deleteCharAt(0).deleteCharAt(letters.length - 1)
                msg.append("* Une durée de ")
                    .append(bonusTimerDuration)
                    .append(" secondes sera ajoutée pour les lettres ")
                    .append(letters)
                    .append("\n")
            } else {
                msg.append("* Pas de temps supplémentaire pour les lettres difficiles\n")
            }
        } else {
            msg.append("* Compte à rebours désactivé\n")
        }

        // compte double
        if (isRandomDoublePointsOn) {
            msg.append("* Chaque manche aura une probabilité de ")
                .append(probabilityPercentage)
                .append("% de compter double pour chaque joueur\n")
        } else {
            msg.append("* Pas d'aléatoire ajouté\n")
        }

        // son
        if (isSoundOn) msg.append("* Sons activés") else msg.append("* Sons désactivés")
        return msg.toString()
    }
}

