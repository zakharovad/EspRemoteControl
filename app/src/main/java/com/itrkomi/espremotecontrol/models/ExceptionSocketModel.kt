package com.itrkomi.espremotecontrol.models

class ExceptionSocketModel(val message: String): BaseWSModel() {
    init {
        type = javaClass.simpleName
    }
}