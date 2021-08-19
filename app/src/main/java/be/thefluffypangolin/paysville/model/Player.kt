package be.thefluffypangolin.paysville.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Cette classe représente un joueur, désigné par son nom.
 */
@Parcelize
data class Player(val name: String) : Parcelable