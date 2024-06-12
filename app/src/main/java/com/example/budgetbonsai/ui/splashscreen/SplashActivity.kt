package com.example.budgetbonsai.ui.splashscreen

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.budgetbonsai.R
import com.example.budgetbonsai.ui.landing.LandingActivity

class SplashActivity : AppCompatActivity() {
    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        viewModel.startSplashTimer()

        viewModel.isTimerFinished.observe(this) { isFinished ->
            if (isFinished) {
                val options = ActivityOptions.makeCustomAnimation(
                    this,
                    android.R.anim.fade_in,
                    android.R.anim.fade_out
                )
                val intent = Intent(this, LandingActivity::class.java)
                startActivity(intent, options.toBundle())
                finish()
            }
        }
    }
}