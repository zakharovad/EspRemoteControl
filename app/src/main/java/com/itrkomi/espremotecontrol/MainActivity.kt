package com.itrkomi.espremotecontrol

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.itrkomi.espremotecontrol.databinding.ActivityMainBinding
import com.itrkomi.espremotecontrol.repos.WSRepository
import com.itrkomi.espremotecontrol.ws.SocketAbortedException
import com.itrkomi.espremotecontrol.ws.WebSocketListener
import com.itrkomi.espremotecontrol.ws.models.SocketUpdate
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.consumeEach
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import com.google.android.material.snackbar.Snackbar
import com.itrkomi.espremotecontrol.models.BaseWSModel
import com.google.gson.Gson
import com.google.gson.JsonIOException
import com.google.gson.JsonSyntaxException
import com.itrkomi.espremotecontrol.models.LedModel
import kotlin.reflect.KClass

class MainActivity : AppCompatActivity(), KodeinAware {
    override val kodein by kodein()
    private lateinit var binding: ActivityMainBinding
    private val webSocketListener: WebSocketListener by instance<WebSocketListener>();
    private val wsRepository: WSRepository by instance<WSRepository>();
    private lateinit var navView: BottomNavigationView;
    private val ledModel: LedModel by instance<LedModel>("LedModel");
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navView = binding.navView
        openWebSocket();
        listenerWebSocket();
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_remote_control, R.id.navigation_notifications))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
    private fun showMessage(text: String){
        Snackbar.make(navView , text, Snackbar.LENGTH_LONG).show()
    }
    private fun showMessage(text: String, actionText: String, callBack:() -> Unit){
        /*val onClick:  (view:View) -> Unit = {
            callBack()
        }*/
        Snackbar.make(navView , text, Snackbar.LENGTH_INDEFINITE)
            .setAction(actionText) {
                callBack();
            }.show();
    }

    private fun openWebSocket(){
        showMessage("Подключение к плате...");
        wsRepository.startSocket(webSocketListener)
    }

    private fun closeWebSocket(){
        wsRepository.closeSocket()
    }
    private fun listenerWebSocket(){
        //Сделаьб гормальную реализацию
        CoroutineScope(Dispatchers.Main).launch {
            try {
                webSocketListener.eventBus.events.consumeEach {
                  if(it.text !== null){
                      if(it.text == "OpenSocket"){
                          showMessage("Соединение установлено");
                      }else{
                          Log.d("parse json: ",it.text)
                          var gson = Gson()
                          try{
                              var model = gson.fromJson(it.text, BaseWSModel::class.java)
                              val ledModelClass = LedModel::class.java
                              when(model.type){
                                  ledModelClass.simpleName ->{
                                      var lModel = gson.fromJson(it.text, ledModelClass)
                                      ledModel.brightness = lModel.brightness
                                  }
                              }
                          }catch(e: JsonSyntaxException){
                              Log.d("JsonSyntaxException","============================")
                          }

                      }

                  }
                   else if(it.exception is SocketAbortedException){
                       showMessage("Соединение разорвано","Повторить", ::openWebSocket)
                   }else if(it.exception !== null){
                       it.exception.message?.let { it1 -> Log.e("Error: ", it1) }
                       showMessage("Ошибка подключения","Повторить", ::openWebSocket)

                   }
                }
            } catch (ex: java.lang.Exception) {
                showMessage("Ошибка подключения","Повторить", ::openWebSocket)
            }
        }
    }
}