package com.itrkomi.espremotecontrol.ui.remote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.itrkomi.espremotecontrol.models.BuzzerModel
import com.itrkomi.espremotecontrol.models.DriveModel
import com.itrkomi.espremotecontrol.models.LedModel

class RemoteControlViewModelFactory(private val ledModel: LedModel, private val driveModel: DriveModel, private val buzzerModel: BuzzerModel): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RemoteControlViewModel::class.java)) {
            return RemoteControlViewModel(
                ledModel,
                driveModel,
                buzzerModel
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}