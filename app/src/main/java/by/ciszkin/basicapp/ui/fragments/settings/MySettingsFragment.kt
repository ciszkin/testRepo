package by.ciszkin.basicapp.ui.fragments.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import by.ciszkin.basicapp.R

class MySettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }
}