package com.developer.finance

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.developer.finance.common.MainActivity

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        observeThemeMode()

        window.setFlags(
            1024,
            1024
        )

        val layout = findViewById<View>(R.id.splash_screen)
        layout.alpha = 1f
        layout.animate().setDuration(1500).alpha(0f).withEndAction {
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        }
    }

    private fun observeThemeMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }
}