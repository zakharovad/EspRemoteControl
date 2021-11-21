package com.itrkomi.espremotecontrol.ui.remote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.itrkomi.espremotecontrol.ws.WebSocketListener

class RemoteControlViewModelFactory(private val wbSocketListener: WebSocketListener): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RemoteControlViewModel::class.java)) {
            return RemoteControlViewModel(
                wbSocketListener
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}