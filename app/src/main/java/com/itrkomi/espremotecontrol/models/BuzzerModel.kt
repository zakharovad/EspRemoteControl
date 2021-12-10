package com.itrkomi.espremotecontrol.models

import android.os.Parcelable
import androidx.databinding.Bindable
import com.itrkomi.espremotecontrol.BR
import kotlinx.android.parcel.Parcelize

@Parcelize
class BuzzerModel : BaseWSModel(), Parcelable {
    init {
        type = javaClass.simpleName
    }

    val frequences:IntArray  = intArrayOf(392, 392, 392, 311, 466, 392, 311, 466, 392,
    587, 587, 587, 622, 466, 369, 311, 466, 392,
    784, 392, 392, 784, 739, 698, 659, 622, 659,
    415, 554, 523, 493, 466, 440, 466,
    311, 369, 311, 466, 392)
    val durations:IntArray  = intArrayOf(350, 350, 350, 250, 100, 350, 250, 100, 700,
        350, 350, 350, 250, 100, 350, 250, 100, 700,
        350, 250, 100, 350, 250, 100, 100, 100, 450,
        150, 350, 250, 100, 100, 100, 450,
        150, 350, 250, 100, 750)

    fun <T:BuzzerModel> update(from:T){
        this.active = from?.active
    }
    @Bindable
    var active: Boolean = false
        set(value){
            if(field == value){
                return
            }
            field = value;
            notifyPropertyChanged(BR.active)
        }
}