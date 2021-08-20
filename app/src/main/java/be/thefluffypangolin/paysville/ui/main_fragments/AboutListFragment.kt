package be.thefluffypangolin.paysville.ui.main_fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import be.thefluffypangolin.paysville.BuildConfig
import be.thefluffypangolin.paysville.R
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity

class AboutListFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.about, rootKey)

        findPreference<Preference>("app_version")?.summary = BuildConfig.VERSION_NAME

        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.email_adress)))
        }
        findPreference<Preference>("contact")?.intent = emailIntent

        val githubIntent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.github_url)))
        findPreference<Preference>("github")?.intent = githubIntent

        val librariesIntent = Intent(requireContext(), OssLicensesMenuActivity::class.java)
        findPreference<Preference>("libraries")?.intent = librariesIntent
        OssLicensesMenuActivity.setActivityTitle(getString(R.string.open_source_libraires))
    }
}