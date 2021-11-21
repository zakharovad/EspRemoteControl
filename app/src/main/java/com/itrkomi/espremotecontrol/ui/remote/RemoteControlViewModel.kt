package com.itrkomi.espremotecontrol.ui.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.itrkomi.espremotecontrol.ui.base.BaseViewModel
import com.itrkomi.espremotecontrol.ws.WebSocketListener
import com.itrkomi.espremotecontrol.ws.models.SocketUpdate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch

class RemoteControlViewModel(private val webSocketListener: WebSocketListener) : BaseViewModel() {

    private val _state = MutableLiveData<SocketUpdate>().apply {
        value = null
    }
    val state:LiveData<SocketUpdate?> = _state
    @ExperimentalCoroutinesApi
    fun subscribeToSocketEvents() {
        ioScope.launch {
            try {
                webSocketListener.eventBus.events.consumeEach {
                    _state.postValue(it);
                }
            } catch (ex: java.lang.Exception) {
                onSocketError(ex)
            }
        }
    }

    private fun onSocketError(ex: Throwable) {
        println("RemoteControlViewModel Error occurred : ${ex.message}")
    }

    override fun onCleared() {
        //repository.closeSocket()
        super.onCleared()

    }
}