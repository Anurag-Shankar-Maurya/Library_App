package com.babumushai.library.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.babumushai.library.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({
            val intent = Intent(this@SplashActivity, LogInActivity::class.java)
            startActivity(intent)

        }, 1000)
    }

    override fun onPause() {
        super.onPause()
        finish()
    }
}