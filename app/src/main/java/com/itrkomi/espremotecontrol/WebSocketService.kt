package com.itrkomi.espremotecontrol

import android.app.Service
import android.content.Intent
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.IBinder
import android.util.Log
import android.view.View
import androidx.core.content.PackageManagerCompat
import com.itrkomi.espremotecontrol.repos.WSRepository
import com.itrkomi.espremotecontrol.ws.WebSocketListener
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import androidx.core.content.PackageManagerCompat.LOG_TAG
import androidx.core.content.PackageManagerCompat.LOG_TAG
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.JsonIOException
import com.itrkomi.espremotecontrol.models.*
import com.itrkomi.espremotecontrol.ws.SocketAbortedException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import android.os.Binder





class WebSocketService : Service(), KodeinAware {
    override val kodein by kodein()
    private val webSocketListener: WebSocketListener by instance<WebSocketListener>();
    private val wsRepository: WSRepository by instance<WSRepository>();
    private val mBinder: IBinder = WebSocketBinder()
    val events = Channel<Any>(Channel.CONFLATED)
    override fun onBind(intent: Intent): IBinder {
        return mBinder;
    }
    override fun onCreate() {
        super.onCreate()
        openWebSocket()
        listenerWebSocket();

    }
    override fun onDestroy() {
        super.onDestroy()
        closeWebSocket()

    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }
    fun <T : BaseWSModel> sendMessage(event: T) {
        try{
            val gson = Gson()
            val tempJSON: String = gson.toJson(event)
            wsRepository.sendMessage(tempJSON)
        } catch ( e: JsonIOException) {
            Log.e("sendMessage","Ошибка парсинга JSON")
        }
    }
    fun openWebSocket(){
        wsRepository.startSocket(webSocketListener)
    }
    fun closeWebSocket(){
        wsRepository.closeSocket()
    }
    private fun listenerWebSocket(){
        //Сделать нормальную реализацию
        CoroutineScope(Dispatchers.Main).launch {
            try {
                webSocketListener.eventBus.events.consumeEach {
                    if(it.text !== null){
                        if(it.text == "OpenSocket"){
                           events.send(OpenSocketModel())
                        }else{
                            var gson = Gson()
                            var model = gson.fromJson(it.text, BaseWSModel::class.java)
                            val ledModelClass = LedModel::class.java
                            when(model.type){
                                ledModelClass.simpleName ->{
                                    val lModel = gson.fromJson(it.text, ledModelClass)
                                    events.send(lModel)
                                }
                            }


                        }

                    }
                    else if(it.exception is SocketAbortedException){
                        events.send(AbortSocketModel())
                    }else if(it.exception !== null){
                        it.exception.message?.let {
                            it1->events.send(ExceptionSocketModel(it1))
                        }

                    }
                }
            } catch (ex: java.lang.Exception) {
                ex.message?.let {
                    events.send( ExceptionSocketModel(it))
                }

            }
        }
    }

    inner class WebSocketBinder : Binder() {
        fun getService(): WebSocketService = this@WebSocketService
    }
}