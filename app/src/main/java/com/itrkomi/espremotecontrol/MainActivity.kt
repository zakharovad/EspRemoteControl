package com.itrkomi.espremotecontrol

import android.os.Bundle
import android.util.Log
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

class MainActivity : AppCompatActivity(), KodeinAware {
    override val kodein by kodein()
    private lateinit var binding: ActivityMainBinding
    private val webSocketListener: WebSocketListener by instance<WebSocketListener>();
    private val wsRepository: WSRepository by instance<WSRepository>();
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        openWebSocket();
        listenerWebSocket();
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_remote_control, R.id.navigation_notifications))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
    private fun openWebSocket(){
        wsRepository.startSocket(webSocketListener)
    }
    private fun closeWebSocket(){
        wsRepository.closeSocket()
    }

    private fun listenerWebSocket(){
        CoroutineScope(Dispatchers.Main).launch {
            try {
                webSocketListener.eventBus.events.consumeEach {
                   if(it.text == "OpenSocket"){
                       Toast.makeText(applicationContext, "Соединение установлено", Toast.LENGTH_SHORT).show()
                   }else if(it.exception is SocketAbortedException){
                       Toast.makeText(applicationContext, "Соединение разорвано", Toast.LENGTH_SHORT).show()
                       closeWebSocket();
                       delay(1000)
                       Toast.makeText(applicationContext, "Переподключение...", Toast.LENGTH_SHORT).show()

                       openWebSocket();
                   }else if(it.exception !== null){
                       it.exception.message?.let { it1 -> Log.e("Error: ", it1) }
                       Toast.makeText(applicationContext, "Ошибка подключения", Toast.LENGTH_SHORT).show()
                       closeWebSocket();
                       delay(1000)
                       Toast.makeText(applicationContext, "Переподключение...", Toast.LENGTH_SHORT).show()
                       openWebSocket();
                   }
                }
            } catch (ex: java.lang.Exception) {
                Toast.makeText(applicationContext, ex.message, Toast.LENGTH_SHORT).show()
                openWebSocket();
            }
        }
    }
}