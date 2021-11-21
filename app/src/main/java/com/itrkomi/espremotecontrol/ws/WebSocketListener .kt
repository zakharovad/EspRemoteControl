package com.itrkomi.espremotecontrol.ws

import com.itrkomi.espremotecontrol.ws.models.SocketUpdate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener;


open class WebSocketListener:WebSocketListener() {
    val eventBus:WebSocketEventBus<SocketUpdate>  = WebSocketEventBus(Channel.CONFLATED);
    override fun onOpen(webSocket: WebSocket, response: Response) {
        webSocket.send("Hi, it is remote client")
        GlobalScope.launch {
            eventBus.postEvent(SocketUpdate("OpenSocket"))
        }
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        GlobalScope.launch {
            eventBus.postEvent(SocketUpdate(text))
        }
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        GlobalScope.launch {
            eventBus.postEvent(SocketUpdate(exception = SocketAbortedException()))
        }
        webSocket.close(code,null)
        //eventBus.close()
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        GlobalScope.launch {
            eventBus.postEvent(SocketUpdate(exception = t))
        }
        webSocket.close(1000,null)
    }
}