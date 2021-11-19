package com.itrkomi.espremotecontrol.ws

interface IEventBus<Event> {

    suspend fun postEvent(event: Event)
    fun close()
}