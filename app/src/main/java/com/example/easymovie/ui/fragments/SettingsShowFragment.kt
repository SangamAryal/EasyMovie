package com.example.easymovie.ui.fragments


import android.os.Bundle
import androidx.leanback.preference.LeanbackPreferenceFragmentCompat

import androidx.leanback.preference.LeanbackSettingsFragmentCompat

import androidx.preference.Preference
import androidx.preference.PreferenceDialogFragmentCompat
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceScreen
import com.example.easymovie.R


class SettingsShowFragment : LeanbackSettingsFragmentCompat() {

    override fun onPreferenceStartInitialScreen() {
        startPreferenceFragment(DemoFragment())
    }

    override fun onPreferenceStartFragment(caller: PreferenceFragmentCompat, pref: Preference): Boolean {
        val args = pref.extras
        val f = pref.fragment?.let {
            childFragmentManager.fragmentFactory.instantiate(
                requireActivity().classLoader, it
            )
        }
        if (f != null) {
            f.arguments = args
        }
        if (f != null) {
            f.setTargetFragment(caller, 0)
        }
        if (f is PreferenceFragmentCompat || f is PreferenceDialogFragmentCompat) {
            startPreferenceFragment(f)
        } else {
            if (f != null) {
                startImmersiveFragment(f)
            }
        }
        return true
    }

    override fun onPreferenceStartScreen(caller: PreferenceFragmentCompat, pref: PreferenceScreen): Boolean {
        val fragment = DemoFragment()
        val args = Bundle(1)
        args.putString(PreferenceFragmentCompat.ARG_PREFERENCE_ROOT, pref.key)
        fragment.arguments = args
        startPreferenceFragment(fragment)
        return true
    }
}

/**
 * The fragment that is embedded in SettingsFragment
 */
class DemoFragment : LeanbackPreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        // Load the preferences from an XML resource
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }
}