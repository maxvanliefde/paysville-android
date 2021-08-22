package be.thefluffypangolin.paysville

import android.content.Context
import android.os.Build
import android.view.MenuItem
import androidx.appcompat.app.AppCompatDelegate

object ThemeHelper {
    const val LIGHT_THEME = "light_theme"
    const val DARK_THEME = "dark_theme"
    const val DEFAULT_THEME = "default_theme"
    private const val PREF_THEME_KEY = "themePref"
    private const val SHARED_PREF_KEY = "be.thefluffypangolin.paysville.SHARED_PREF_KEY"

    /**
     * Applique le thème donné, et modifie la valeur en mémoire
     */
    @JvmStatic
    fun modifyTheme(context: Context, theme: String, toggle: MenuItem) {
        applyTheme(theme, toggle)
        val sharedPreferences = context.getSharedPreferences(
            SHARED_PREF_KEY, Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString(PREF_THEME_KEY, theme)
            apply()
        }
    }

    /**
     * Applique le thème en mémoire, ou le thème par défaut le cas échéant
     */
    @JvmStatic
    fun loadTheme(context: Context, toggle: MenuItem) {
        val sharedPreferences = context.getSharedPreferences(
            SHARED_PREF_KEY, Context.MODE_PRIVATE)
        val theme = sharedPreferences.getString(PREF_THEME_KEY, DEFAULT_THEME)!!
        applyTheme(theme, toggle)
    }

    /**
     * Applique le thème donné, sans modifier la valeur en mémoire
     */
    @JvmStatic
    private fun applyTheme(theme: String, toggle: MenuItem) {
        val nightMode: Int; val icon: Int
        when (theme) {
            LIGHT_THEME -> {
                nightMode = AppCompatDelegate.MODE_NIGHT_NO
                icon = R.drawable.ic_theme_light_24
            }
            DARK_THEME -> {
                nightMode = AppCompatDelegate.MODE_NIGHT_YES
                icon = R.drawable.ic_theme_dark_24
            }
            else ->{
                nightMode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                } else {
                    AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
                }
                icon = R.drawable.ic_theme_auto_24
            }
        }
        AppCompatDelegate.setDefaultNightMode(nightMode)
        toggle.setIcon(icon)
    }
}