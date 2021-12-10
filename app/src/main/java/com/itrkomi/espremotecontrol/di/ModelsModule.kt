package com.itrkomi.espremotecontrol.di

import com.itrkomi.espremotecontrol.models.BuzzerModel
import com.itrkomi.espremotecontrol.models.DriveModel
import com.itrkomi.espremotecontrol.models.LedModel
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

private const val MODULE_NAME = "Models Module"
val modelsModule = Kodein.Module(MODULE_NAME, false){
    bind<LedModel>("LedModel") with singleton{ LedModel() }
    bind<DriveModel>("DriveModel") with singleton{ DriveModel() }
    bind<BuzzerModel>("BuzzerModel") with singleton{ BuzzerModel() }
}