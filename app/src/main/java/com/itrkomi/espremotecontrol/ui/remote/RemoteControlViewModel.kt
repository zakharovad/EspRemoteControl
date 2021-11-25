package com.itrkomi.espremotecontrol.ui.remote

import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import com.itrkomi.espremotecontrol.models.LedModel
import com.itrkomi.espremotecontrol.repos.WSRepository
import com.itrkomi.espremotecontrol.ui.base.BaseViewModel

import com.itrkomi.espremotecontrol.ws.models.SocketUpdate
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class RemoteControlViewModel(private val repository: WSRepository, val ledModel: LedModel) : BaseViewModel() {
    lateinit var delayJob: Job
    init {

        ledModel.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                sendMessage(ledModel)
            }
        });
    }
    private fun sendMessage(ledModel: LedModel){
        repository.sendMessage(ledModel)
        /*if(::delayJob.isInitialized)
            delayJob.cancel()
        delayJob = ioScope.launch{
            delay(800)
            repository.sendMessage(ledModel)
        }*/
    }
    private val _state = MutableLiveData<SocketUpdate>().apply {
        value = null
    }


    override fun onCleared() {
        super.onCleared()
    }
}