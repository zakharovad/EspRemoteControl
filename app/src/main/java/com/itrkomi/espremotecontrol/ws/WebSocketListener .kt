package com.itrkomi.espremotecontrol.ws

import com.itrkomi.espremotecontrol.ws.models.SocketUpdate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener;


@ExperimentalCoroutinesApi
open class WebSocketListener:WebSocketListener() {
    val eventBus:WebSocketEventBus<SocketUpdate>  = WebSocketEventBus(10);
    override fun onOpen(webSocket: WebSocket, response: Response) {
        webSocket.send("Hi")
        webSocket.send("Hi again")
        webSocket.send("Hi again again")
        webSocket.send("Hi again again again")
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
        eventBus.close()
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        GlobalScope.launch {
            eventBus.postEvent(SocketUpdate(exception = t))
        }
    }

}

class SocketAbortedException : Exception()
