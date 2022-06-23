package com.itrkomi.espremotecontrol.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.PreferenceFragmentCompat
import com.itrkomi.espremotecontrol.R
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware


class SettingsFragment : PreferenceFragmentCompat(), KodeinAware {
    override val kodein: Kodein by lazy { (this.context as KodeinAware).kodein }
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = "settingsPreference"
        preferenceManager.sharedPreferencesMode = 0x0000
        setPreferencesFromResource(R.xml.main_settings, rootKey)
    }


}