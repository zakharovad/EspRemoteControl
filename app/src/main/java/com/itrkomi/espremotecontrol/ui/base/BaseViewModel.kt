package com.itrkomi.espremotecontrol.ui.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel

abstract class BaseViewModel: ViewModel() {

    /**
     * This is a scope for all coroutines launched by [BaseViewModel]
     * that will be dispatched in Main Thread
     */
    protected val uiScope = CoroutineScope(Dispatchers.Main)

    /**
     * This is a scope for all coroutines launched by [BaseViewModel]
     * that will be dispatched in a Pool of Thread
     */
    protected val ioScope = CoroutineScope(Dispatchers.Default)

    /**
     * Cancel all coroutines when the ViewModel is cleared
     */
    override fun onCleared() {
        super.onCleared()
        uiScope.coroutineContext.cancel()
        ioScope.coroutineContext.cancel()
    }
}