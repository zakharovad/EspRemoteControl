package com.itrkomi.espremotecontrol.models

import android.os.Parcelable
import androidx.databinding.Bindable
import com.itrkomi.espremotecontrol.BR
import kotlinx.android.parcel.Parcelize

@Parcelize
class DriveModel : BaseWSModel(), Parcelable {
    init {
        type = javaClass.simpleName
    }
    fun <T:DriveModel> update(from:T){
        this.speed = from?.speed
    }
    @Bindable
    var speed: Int = 0
        set(value){
            val coerceValue = value.coerceIn(0, 255);
            if(field == coerceValue){
                return
            }
            field = coerceValue;
            notifyChange()
        }
    @Bindable
    var direction: Int = 0
        set(value){
            if(field == value){
                return
            }
            field = value
            notifyChange()
        }
}