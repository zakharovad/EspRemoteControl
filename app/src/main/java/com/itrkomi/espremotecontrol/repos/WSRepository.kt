package com.itrkomi.espremotecontrol.repos

import com.itrkomi.espremotecontrol.ws.WebSocketEventBus
import com.itrkomi.espremotecontrol.ws.WebSocketListener
import com.itrkomi.espremotecontrol.ws.WebSocketProvider
import com.itrkomi.espremotecontrol.ws.models.SocketUpdate
import kotlinx.coroutines.ExperimentalCoroutinesApi

class WSRepository constructor(private val provider: WebSocketProvider) {

    @ExperimentalCoroutinesApi
    fun startSocket(): WebSocketEventBus<SocketUpdate> =
        provider.startSocket()

    @ExperimentalCoroutinesApi
    fun startSocket(listener:WebSocketListener): WebSocketEventBus<SocketUpdate> {
        return provider.startSocket(listener);
    }


    @ExperimentalCoroutinesApi
    fun closeSocket() {
        provider.stopSocket()
    }

}