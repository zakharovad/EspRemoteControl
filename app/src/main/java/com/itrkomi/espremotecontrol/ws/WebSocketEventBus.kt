package com.itrkomi.espremotecontrol.ws
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class WebSocketEventBus<Event>(capacity:Int ): IEventBus<Event>{
    private val _events = Channel<Event>(capacity)
    val events = _events.receiveAsFlow()
    override suspend fun postEvent(event: Event){
        _events.send(event)
    }
    override  fun close(){
        _events.close()
    }

}