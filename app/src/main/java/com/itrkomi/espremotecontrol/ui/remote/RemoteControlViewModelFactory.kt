package com.itrkomi.espremotecontrol.ui.remote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.itrkomi.espremotecontrol.models.LedModel
import com.itrkomi.espremotecontrol.repos.WSRepository

class RemoteControlViewModelFactory(private val repository: WSRepository, private val ledModel: LedModel): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RemoteControlViewModel::class.java)) {
            return RemoteControlViewModel(
                repository,
                ledModel,
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}