package com.itrkomi.espremotecontrol.ui.remote

import RepeatListener
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import com.itrkomi.espremotecontrol.WebSocketService
import com.itrkomi.espremotecontrol.models.BaseWSModel
import com.itrkomi.espremotecontrol.models.DriveModel
import com.itrkomi.espremotecontrol.models.LedModel
import com.itrkomi.espremotecontrol.repos.WSRepository
import com.itrkomi.espremotecontrol.ui.base.BaseViewModel

import com.itrkomi.espremotecontrol.ws.models.SocketUpdate
import kotlinx.coroutines.*


class RemoteControlViewModel( val ledModel: LedModel,  val driveModel: DriveModel) : BaseViewModel() {
    private var wsService:WebSocketService? = null

    enum class Direction {
        F, B, L, R
    }
    private val  step:Int = 10;
    init {
        ledModel.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                sendMessage(ledModel)
            }
        });
        driveModel.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                if(driveModel.direction  === 0){
                    speedChange(0)
                }
                sendMessage(driveModel)
            }
        });

    }
    fun addWsService(service:WebSocketService){
        wsService = service
    }
    private fun sendMessage(model: BaseWSModel){
        wsService?.sendMessage(model)
    }

    fun speedChange(value:Int? = null) {
        driveModel.speed  = if(value !== null) value.coerceIn(0, 255) else driveModel.speed+step
        /*if(::speedChangeJob.isInitialized){
            speedChangeJob.cancel()
        }*/
        /*speedChangeJob = ioScope.launch (ioScope.coroutineContext + SupervisorJob()){
            delay(100)
            driveModel.speed  = value.coerceIn(0, 255);
        }*/
    }

    fun removeDirectionBit(directionType: Direction = Direction.F){

        when(directionType){
            Direction.F ->{
                driveModel.direction =  driveModel.direction and(14)
            }
            Direction.B ->{
                driveModel.direction =  driveModel.direction and(13)
            }
            Direction.L ->{
                driveModel.direction =  driveModel.direction and(11)
            }
            Direction.R ->{
                driveModel.direction =  driveModel.direction and(7)
            }

        }
    }

    fun setDirectionBit(directionType: Direction = Direction.F){

        when(directionType){
            Direction.F ->{
                driveModel.direction =  driveModel.direction or(1) and(13)
            }
            Direction.B ->{
                driveModel.direction =  driveModel.direction or(2) and(14)
            }
            Direction.L ->{
                driveModel.direction =  driveModel.direction or(4) and(7)
            }
            Direction.R ->{
                driveModel.direction =  driveModel.direction or(8) and(11)
            }


        }

    }

    fun bindForwardButton(button: View){
        val listener = RepeatListener(400, 100,
            object:View.OnClickListener{
                override fun onClick(p0: View?) {
                    setDirectionBit(Direction.F)
                    speedChange();
                }
            },
            object:View.OnClickListener{
                override fun onClick(p0: View?) {
                    removeDirectionBit(Direction.F)
                }
            })
        button.setOnTouchListener(listener);
    }

    fun bindBackButton(button: View){
        val listener = RepeatListener(400, 100,
            object:View.OnClickListener{
                override fun onClick(p0: View?) {
                    setDirectionBit(Direction.B)
                    speedChange(driveModel.speed+step)
                }
            },
            object:View.OnClickListener{
                override fun onClick(p0: View?) {
                    removeDirectionBit(Direction.B)
                }
            })
        button.setOnTouchListener(listener);
    }

    fun bindLeftButton(button: View){
        val listener = RepeatListener(400, 100,
            object:View.OnClickListener{
                override fun onClick(p0: View?) {
                    setDirectionBit(Direction.L)
                    speedChange()
                }
            },
            object:View.OnClickListener{
                override fun onClick(p0: View?) {
                    removeDirectionBit(Direction.L)
                }
            })
        button.setOnTouchListener(listener);
    }

    fun bindRightButton(button: View){
        val listener = RepeatListener(400, 100,
            object:View.OnClickListener{
                override fun onClick(p0: View?) {
                    setDirectionBit(Direction.R)
                    speedChange()
                }
            },
            object:View.OnClickListener{
                override fun onClick(p0: View?) {
                    removeDirectionBit(Direction.R)
                }
            })
        button.setOnTouchListener(listener);
    }


}