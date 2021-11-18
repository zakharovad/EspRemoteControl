package com.itrkomi.espremotecontrol.app

import android.app.Application
import android.content.Context
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton
class App: Application(), KodeinAware {
    override val kodein = Kodein {
        import(androidXModule(this@App))
        bind<Context>("ApplicationContext") with singleton { this@App.applicationContext }
        bind<App>() with singleton { this@App }
    }
}