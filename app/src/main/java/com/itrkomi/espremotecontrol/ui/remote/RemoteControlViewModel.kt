package com.itrkomi.espremotecontrol.ui.remote

import android.view.MotionEvent
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import com.itrkomi.espremotecontrol.WebSocketService
import com.itrkomi.espremotecontrol.models.DriveModel
import com.itrkomi.espremotecontrol.models.LedModel
import com.itrkomi.espremotecontrol.repos.WSRepository
import com.itrkomi.espremotecontrol.ui.base.BaseViewModel

import com.itrkomi.espremotecontrol.ws.models.SocketUpdate
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class RemoteControlViewModel( val ledModel: LedModel,  val driveModel: DriveModel) : BaseViewModel() {
    private var wsService:WebSocketService? = null
    init {

        ledModel.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                sendMessage(ledModel)
            }
        });
    }
    fun addWsService(service:WebSocketService){
        wsService = service
    }
    private fun sendMessage(ledModel: LedModel){
        wsService?.sendMessage(ledModel)
    }
    fun driveModelSpeedUp() {
        driveModel.speed += 20;

    }
    fun driveModelSpeedZero() {
        driveModel.speed  = 0;

    }
}