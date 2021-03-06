package com.itrkomi.espremotecontrol.ws

import com.itrkomi.espremotecontrol.ws.models.SocketUpdate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import java.util.concurrent.TimeUnit

class WebSocketProvider {

    private var _webSocket: WebSocket? = null

    private val socketOkHttpClient = OkHttpClient.Builder()
        .readTimeout(30, TimeUnit.SECONDS)
        .connectTimeout(10, TimeUnit.SECONDS)
        //.hostnameVerifier { _, _ -> false }
        .build()

    @ExperimentalCoroutinesApi
    private var _webSocketListener: WebSocketListener? = null

    @ExperimentalCoroutinesApi
    fun startSocket(url:String): WebSocketEventBus<SocketUpdate> =
        with(WebSocketListener()) {
            startSocket(url,this)
            this@with.eventBus
    }

    @ExperimentalCoroutinesApi
    fun startSocket(url:String, webSocketListener: WebSocketListener): WebSocketEventBus<SocketUpdate> {
        _webSocketListener = webSocketListener
        _webSocket = socketOkHttpClient.newWebSocket(
            Request.Builder().url(url).build(),
            webSocketListener
        )
        socketOkHttpClient.connectionPool.evictAll();
        //socketOkHttpClient.dispatcher.executorService.shutdown()
        return webSocketListener.eventBus
    }
    @ExperimentalCoroutinesApi
    fun sendMessage(message: String) {
        _webSocket?.send(message)
    }
    @ExperimentalCoroutinesApi
    fun stopSocket() {
        try {

            _webSocket?.cancel()
            _webSocket?.close(NORMAL_CLOSURE_STATUS, null)
            _webSocket = null
            //_webSocketListener?.eventBus?.close()
            //_webSocketListener = null
        } catch (ex: Exception) {
        }
    }

    companion object {
        const val NORMAL_CLOSURE_STATUS = 1000
    }

}