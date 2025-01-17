package com.waytruck.cargo.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsetsController
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.nucleoti.lumicenter.ui.utils.PreferencesController
import com.waytruck.cargo.R
import com.waytruck.cargo.databinding.ActivitySplashBinding
import com.waytruck.cargo.ui.home.MainActivity
import com.waytruck.cargo.ui.login.LoginActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

//@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private val splashScreenDuration = 3000L // 3 segundos

    // Número de teléfono al que deseas enviar el mensaje
    var preferencesController: PreferencesController? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferencesController = PreferencesController(this)
        statusBar()

        // Usar Coroutine para el retraso
        CoroutineScope(Dispatchers.Main).launch {
            delay(splashScreenDuration)
            if (preferencesController!!.isLogin()) {
                startHome()
            } else {
                startLogin()
            }

        }
    }

    fun startLogin() {
        val intent = Intent(this@SplashActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun startHome() {
        val intent = Intent(this@SplashActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun statusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
            window.setDecorFitsSystemWindows(false)
            window.insetsController?.setSystemBarsAppearance(
                0,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
            window.statusBarColor = ContextCompat.getColor(this, R.color.yellow)
        } else {
            window.statusBarColor = ContextCompat.getColor(this, R.color.yellow)
        }
    }
}