package com.itrkomi.espremotecontrol.ui.remote

import RepeatListener
import android.view.MotionEvent
import android.view.View
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
    private val  step:Int = 10;
    private val _btnPressedIndicator:MutableLiveData<Int> = MutableLiveData<Int>(0);
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
        driveModel.speed += step;2
    }
    fun driveModelSpeedZero() {
        driveModel.speed  = 0;
    }
    fun bindForwardButton(button: View){
        val listener = RepeatListener(400, 100,
            object:View.OnClickListener{
                override fun onClick(p0: View?) {
                    var curIndicate = _btnPressedIndicator.value;

                    _btnPressedIndicator.postValue(_btnPressedIndicator.value)
                    driveModelSpeedUp()
                }
            },
            object:View.OnClickListener{
                override fun onClick(p0: View?) {
                    driveModelSpeedZero()
                }
            })
        button.setOnTouchListener(listener);
    }
    fun bindBackButton(button: View){
        val listener = RepeatListener(400, 100,
            object:View.OnClickListener{
                override fun onClick(p0: View?) {
                    driveModelSpeedUp()
                }
            },
            object:View.OnClickListener{
                override fun onClick(p0: View?) {
                    driveModelSpeedZero()
                }
            })
        button.setOnTouchListener(listener);
    }
    fun bindLeftButton(button: View){
        val listener = RepeatListener(400, 100,
            object:View.OnClickListener{
                override fun onClick(p0: View?) {
                    driveModelSpeedUp()
                }
            },
            object:View.OnClickListener{
                override fun onClick(p0: View?) {
                    driveModelSpeedZero()
                }
            })
        button.setOnTouchListener(listener);
    }
    fun bindRightButton(button: View){
        val listener = RepeatListener(400, 100,
            object:View.OnClickListener{
                override fun onClick(p0: View?) {
                    driveModelSpeedUp()
                }
            },
            object:View.OnClickListener{
                override fun onClick(p0: View?) {
                    driveModelSpeedZero()
                }
            })
        button.setOnTouchListener(listener);
    }


}