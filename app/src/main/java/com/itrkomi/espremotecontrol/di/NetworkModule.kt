package com.itrkomi.espremotecontrol.di

import com.itrkomi.espremotecontrol.repos.WSRepository
import com.itrkomi.espremotecontrol.ws.WebSocketListener
import com.itrkomi.espremotecontrol.ws.WebSocketProvider

import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

private const val MODULE_NAME = "Network Module"
val networkModule = Kodein.Module(MODULE_NAME, false) {
    bind<WebSocketListener>() with singleton { WebSocketListener() }
    bind<WebSocketProvider>() with singleton { WebSocketProvider() }
    bind<WSRepository>() with singleton { getWSRepository(instance()) }

};
private fun getWSRepository(provider: WebSocketProvider): WSRepository {
    return WSRepository(provider)
}


