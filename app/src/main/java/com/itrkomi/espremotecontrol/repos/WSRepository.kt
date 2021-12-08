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
import com.itrkomi.espremotecontrol.ws.SocketAbortedException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch

class WSRepository constructor(private val provider: WebSocketProvider) {

    @ExperimentalCoroutinesApi
    fun startSocket(): WebSocketEventBus<SocketUpdate> =
        provider.startSocket()
    private var _isStarted:Boolean = false;
    fun isStarted():Boolean{
        return _isStarted
    }
    @ExperimentalCoroutinesApi
    fun startSocket(listener:WebSocketListener): WebSocketEventBus<SocketUpdate> {
        val eventBus: WebSocketEventBus<SocketUpdate> = provider.startSocket(listener);
        CoroutineScope(Dispatchers.Main).launch{
            for (event in eventBus.events) {
                if(event.text == "OpenSocket"){
                    _isStarted = true;
                }else if(event.exception != null){
                    _isStarted = false;
                }
                eventBus.events.send(event)
            }
        }
        return eventBus;
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