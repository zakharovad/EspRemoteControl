package com.itrkomi.espremotecontrol

import android.content.Intent
import android.graphics.drawable.Animatable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.itrkomi.espremotecontrol.databinding.ActivitySplashScreenBinding

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        (binding.splashScreenImage.drawable as? Animatable)?.start()

        val intent = Intent(this, MainActivity::class.java)
        CoroutineScope(Dispatchers.Main).launch{
            delay(3000)
            startActivity(intent)
            finish()
        }
    }
}
