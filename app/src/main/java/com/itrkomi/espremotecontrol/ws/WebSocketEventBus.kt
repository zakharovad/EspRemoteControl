package com.itrkomi.espremotecontrol.ws
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class WebSocketEventBus<Event>(capacity:Int ): IEventBus<Event>{
    val events = Channel<Event>(capacity)
    //val events = _events.receiveAsFlow()
    override suspend fun postEvent(event: Event){
        events.send(event)
    }
    override  fun close(){
        events.close()
    }

}