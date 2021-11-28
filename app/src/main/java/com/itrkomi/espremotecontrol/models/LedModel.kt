package com.itrkomi.espremotecontrol.models

import android.os.Parcelable
import androidx.databinding.Bindable
import com.itrkomi.espremotecontrol.BR
import kotlinx.android.parcel.Parcelize

@Parcelize
class LedModel : BaseWSModel(), Parcelable {
    init {
        type = javaClass.simpleName
    }
    fun <T:LedModel> update(from:T){
        this.brightness = from?.brightness
    }
    @Bindable
    var brightness: Int = 0
        set(value){
            if(field == value){
                return
            }
            field = value;
            notifyPropertyChanged(BR.brightness)
        }
}