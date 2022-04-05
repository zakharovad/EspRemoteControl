package com.itrkomi.espremotecontrol.di

import com.itrkomi.espremotecontrol.repos.SettingsRepository
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

private const val MODULE_NAME = "Settings Module";
val settingsModule = Kodein.Module(MODULE_NAME, false){
    bind<SettingsRepository>("settingsPreference") with singleton { SettingsRepository(instance("ApplicationContext"),"settingsPreference") }
}
