package com.chatwith.app.ui.splash_screen

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.chatwith.app.MainActivity
import com.chatwith.app.R
import com.chatwith.app.databinding.ActivitySplashScreenBinding
import com.chatwith.app.ui.sigh_up_and_login.SignUp

class SplashScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    private var timeOut = 1300
    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = this.resources.getColor(R.color.colorPrimary, null)
        val alpha = PropertyValuesHolder.ofFloat("Alpha", 0f, 1f)
        val translateY = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, -200f, 0f)
        ObjectAnimator.ofPropertyValuesHolder(binding.imgLogo, alpha, translateY).apply {
            duration = 800
            start()
        }
        ObjectAnimator.ofPropertyValuesHolder(binding.text, alpha).apply {
            duration = 1000
            start()

        }

        Handler().postDelayed({
            startActivity(Intent(this@SplashScreen, SignUp::class.java))
            finish()
        }, timeOut.toLong())
    }
}
