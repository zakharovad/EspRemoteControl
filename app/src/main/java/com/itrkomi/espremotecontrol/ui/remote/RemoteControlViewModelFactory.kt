package com.itrkomi.espremotecontrol.ui.remote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.itrkomi.espremotecontrol.WebSocketService
import com.itrkomi.espremotecontrol.models.LedModel
import com.itrkomi.espremotecontrol.repos.WSRepository

class RemoteControlViewModelFactory(private val ledModel: LedModel): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RemoteControlViewModel::class.java)) {
            return RemoteControlViewModel(
                ledModel,
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}