package com.itrkomi.espremotecontrol

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
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
import com.itrkomi.espremotecontrol.models.ExceptionSocketModel
import com.itrkomi.espremotecontrol.models.LedModel
import com.itrkomi.espremotecontrol.models.OpenSocketModel
import kotlin.reflect.KClass

class MainActivity : AppCompatActivity(), KodeinAware {
    override val kodein by kodein()
    private lateinit var binding: ActivityMainBinding
    private var  wsService: WebSocketService? = null;
    private val connection  = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName?, service: IBinder) {
            val binder = service as WebSocketService.WebSocketBinder
            wsService = binder.getService()
            listenerWebSocket()
        }
        override fun onServiceDisconnected(className: ComponentName) {
            wsService = null
        }
    }
    private lateinit var navView: BottomNavigationView;
    private val ledModel: LedModel by instance<LedModel>("LedModel");
    override fun onStart() {
        super.onStart()
        // Bind to WebSocketService
        Intent(this, WebSocketService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_remote_control, R.id.navigation_notifications))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun showMessage(text: String){
        Snackbar.make(navView , text, Snackbar.LENGTH_LONG).show()
    }

    private fun showMessage(text: String, actionText: String, callBack:() -> Unit){
        Snackbar.make(navView , text, Snackbar.LENGTH_INDEFINITE)
            .setAction(actionText) {
                callBack();
            }.show();
    }

    private fun openWebSocket(){
        showMessage("Подключение к плате...");
        wsService?.openWebSocket()
    }

    private fun closeWebSocket(){
        wsService?.closeWebSocket()
    }
    private fun listenerWebSocket(){
        //Сделать нормальную реализацию
        CoroutineScope(Dispatchers.Main).launch {
            try {
                wsService?.events?.consumeEach {
                    when(it){
                        is OpenSocketModel->{
                            showMessage("Соединение установлено");
                        }
                        is LedModel->{
                            ledModel.update(it)
                        }
                        is SocketAbortedException->{
                            showMessage("Соединение разорвано","Повторить", ::openWebSocket)
                        }
                        is ExceptionSocketModel ->{
                            showMessage(it.message,"Повторить", ::openWebSocket)
                        }

                    }
                }
            } catch (ex: java.lang.Exception) {
                showMessage("Упс, что то пошло не так! Попробуй переподключиться","Повторить", ::openWebSocket)
            }
        }
    }
}