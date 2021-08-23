package be.thefluffypangolin.paysville.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.lang.IndexOutOfBoundsException

/**
 * Génère des manches comprenant une lettre aléatoire chacune.
 */
@Parcelize
class PaysVilleRoundFactory(
    private val letters: List<Char> = ('A'..'Z').toMutableList().apply { shuffle() }.toList(),
    private var currentIndex: Int = 0
) : Parcelable {
    fun newRound(): PaysVilleRound {
        try {
            return PaysVilleRound(letters[currentIndex++])
        } catch (e: IndexOutOfBoundsException) {
            throw PaysVilleGame.NoLetterLeftException()
        }
    }
}