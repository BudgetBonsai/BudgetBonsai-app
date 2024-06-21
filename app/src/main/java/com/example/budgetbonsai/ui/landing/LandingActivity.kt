package com.example.budgetbonsai.ui.landing

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.budgetbonsai.R
import com.example.budgetbonsai.ViewModelFactory
import com.example.budgetbonsai.data.local.UserPreference
import com.example.budgetbonsai.data.local.dataStore
import com.example.budgetbonsai.databinding.ActivityLandingBinding
import com.example.budgetbonsai.ui.MainActivity
import com.example.budgetbonsai.ui.login.LoginActivity
import com.example.budgetbonsai.ui.login.LoginViewModel
import com.example.budgetbonsai.ui.register.RegisterActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LandingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLandingBinding
    private lateinit var userPreference: UserPreference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLandingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val viewModelFactory = ViewModelFactory.getInstance(this)
        val viewModel: LoginViewModel by viewModels { viewModelFactory}

        userPreference = UserPreference.getInstance(dataStore)
        checkSessionAndRedirect()

        binding.btnSignup.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private var doubleBackToExitPressedOnce = false

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun checkSessionAndRedirect() {
        CoroutineScope(Dispatchers.Main).launch {
            if (userPreference.isSessionActive()) {
                goToMainActivity()
            }
        }
    }

    override fun onBackPressed() {
        Toast.makeText(this, "Press back again to close the app", Toast.LENGTH_SHORT).show()

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Handler().postDelayed({doubleBackToExitPressedOnce = false
        }, 2000)
    }
}