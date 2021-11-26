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
    @Bindable
    var brightness: Int = 100
        set(value){
            if(field == value){
                return
            }
            field = value;
            notifyPropertyChanged(BR.brightness)
        }
}