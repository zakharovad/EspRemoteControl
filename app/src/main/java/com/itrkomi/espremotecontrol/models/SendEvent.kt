package com.itrkomi.espremotecontrol.models

import androidx.databinding.BaseObservable

abstract  class SendEvent: BaseObservable() {
    open var type:String =""
}