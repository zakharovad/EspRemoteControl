package com.itrkomi.espremotecontrol.repos

import android.util.Log
import com.itrkomi.espremotecontrol.models.BaseWSModel
import com.itrkomi.espremotecontrol.ws.WebSocketEventBus
import com.itrkomi.espremotecontrol.ws.WebSocketListener
import com.itrkomi.espremotecontrol.ws.WebSocketProvider
import com.itrkomi.espremotecontrol.ws.models.SocketUpdate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.google.gson.Gson
import com.google.gson.JsonIOException

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

    fun sendMessage(message: String) {
        provider.sendMessage(message)
    }
    fun <T : BaseWSModel> sendMessage(event: T) {
        try{
            val gson = Gson()
            val tempJSON: String = gson.toJson(event)
        provider.sendMessage(tempJSON)
        } catch ( e:JsonIOException) {
            Log.e("sendMessage","Ошибка парсинга JSON")
        }
    }

}