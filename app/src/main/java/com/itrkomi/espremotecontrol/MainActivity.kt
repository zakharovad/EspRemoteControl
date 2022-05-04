package com.itrkomi.espremotecontrol

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.itrkomi.espremotecontrol.databinding.ActivityMainBinding
import com.itrkomi.espremotecontrol.ws.SocketAbortedException
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.consumeEach
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import com.google.android.material.snackbar.Snackbar
import com.itrkomi.espremotecontrol.models.ExceptionSocketModel
import com.itrkomi.espremotecontrol.models.LedModel
import com.itrkomi.espremotecontrol.models.OpenSocketModel
import android.widget.LinearLayout




class MainActivity : AppCompatActivity(), KodeinAware {
    override val kodein by kodein()
    private lateinit var binding: ActivityMainBinding
    private var  wsService: WebSocketService? = null;
    private val connection  = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName?, service: IBinder) {
            val binder = service as WebSocketService.WebSocketBinder
            wsService = binder.getService()
            openWebSocket()
            listenerWebSocket()
        }
        override fun onServiceDisconnected(className: ComponentName) {
            wsService = null
        }
    }
    private lateinit var navView: BottomNavigationView;
    private lateinit var flButton: FloatingActionButton
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
        flButton = binding.connectButton
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.navigation_remote_control))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        flButton.setOnClickListener { view ->
            closeWebSocket();
            openWebSocket();
            listenerWebSocket();
        }
    }

    private fun showMessage(text: String){
        Snackbar.make(flButton , text, Snackbar.LENGTH_LONG)
            .setAnchorView(flButton)
            .show()
    }

    private fun showMessage(text: String, actionText: String, callBack:() -> Unit){
        Snackbar.make(flButton , text, Snackbar.LENGTH_INDEFINITE)
            .setAnchorView(flButton)
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
                            showMessage("Соединение разорвано")
                        }
                        is ExceptionSocketModel ->{
                            showMessage(it.message)
                        }

                    }
                }
            } catch (ex: java.lang.Exception) {
                showMessage("Упс, что то пошло не так! Попробуй переподключиться","Повторить", ::openWebSocket)
            }
        }
    }
}