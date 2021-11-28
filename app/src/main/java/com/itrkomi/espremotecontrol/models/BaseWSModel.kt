package com.itrkomi.espremotecontrol.models

import androidx.databinding.BaseObservable

open class BaseWSModel: BaseObservable() {
    open var type:String =""
    fun <T:BaseWSModel> update(from:T){}
}