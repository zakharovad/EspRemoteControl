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

    enum class Direction {
        F, B, L, R
    }
    private val  step:Int = 10;
    private val _btnPressedIndicator:MutableLiveData<Int> = MutableLiveData(0);
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
    fun removeDirectionBit(value:Int?, direction: Direction = Direction.F): Int{
        if(value === null){
            return 0
        }
        when(direction){
            Direction.F ->{
                return value and(14)
            }
            Direction.B ->{
                return value and(13)
            }
            Direction.L ->{
                return value and(11)
            }
            Direction.R ->{
                return value and(7)
            }

        }
        return 0
    }

    fun setDirectionBit(value:Int?, direction: Direction = Direction.F): Int{
        if(value === null){
            return 0
        }
        when(direction){
            Direction.F ->{
                return value or(1) and(13)
            }
            Direction.B ->{
                return value or(2) and(14)
            }
            Direction.L ->{
                return value or(4) and(7)
            }
            Direction.R ->{
                return value or(8) and(11)
            }


        }
        return 0
    }

    fun bindForwardButton(button: View){
        val listener = RepeatListener(400, 100,
            object:View.OnClickListener{
                override fun onClick(p0: View?) {
                    driveModel.direction = setDirectionBit(driveModel.direction, Direction.F)
                    driveModelSpeedUp()
                }
            },
            object:View.OnClickListener{
                override fun onClick(p0: View?) {
                    driveModel.direction = removeDirectionBit(driveModel.direction, Direction.F)
                    driveModelSpeedZero()
                }
            })
        button.setOnTouchListener(listener);
    }

    fun bindBackButton(button: View){
        val listener = RepeatListener(400, 100,
            object:View.OnClickListener{
                override fun onClick(p0: View?) {
                    driveModel.direction =setDirectionBit(driveModel.direction, Direction.B)
                    driveModelSpeedUp()
                }
            },
            object:View.OnClickListener{
                override fun onClick(p0: View?) {
                    driveModel.direction = removeDirectionBit(driveModel.direction, Direction.B)
                    driveModelSpeedZero()
                }
            })
        button.setOnTouchListener(listener);
    }

    fun bindLeftButton(button: View){
        val listener = RepeatListener(400, 100,
            object:View.OnClickListener{
                override fun onClick(p0: View?) {
                    driveModel.direction = setDirectionBit(driveModel.direction, Direction.L)
                    driveModelSpeedUp()
                }
            },
            object:View.OnClickListener{
                override fun onClick(p0: View?) {
                    driveModel.direction = removeDirectionBit(driveModel.direction, Direction.L)
                    driveModelSpeedZero()
                }
            })
        button.setOnTouchListener(listener);
    }

    fun bindRightButton(button: View){
        val listener = RepeatListener(400, 100,
            object:View.OnClickListener{
                override fun onClick(p0: View?) {
                    driveModel.direction = setDirectionBit(driveModel.direction, Direction.R)
                    driveModelSpeedUp()
                }
            },
            object:View.OnClickListener{
                override fun onClick(p0: View?) {
                    driveModel.direction = removeDirectionBit(driveModel.direction, Direction.R)
                    driveModelSpeedZero()
                }
            })
        button.setOnTouchListener(listener);
    }


}