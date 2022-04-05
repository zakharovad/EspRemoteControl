package com.itrkomi.espremotecontrol.models
import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.itrkomi.espremotecontrol.BR
import kotlinx.android.parcel.Parcelize

@Parcelize
class SettingsModel: BaseObservable(), Parcelable {
    @Bindable
    var address: String = ""
        set(value){
            if(field == value){
                return
            }
            field = value;
            notifyPropertyChanged(BR.address)
        }
}