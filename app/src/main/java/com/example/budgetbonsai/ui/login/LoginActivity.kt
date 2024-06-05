package com.example.budgetbonsai.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.budgetbonsai.R
import com.example.budgetbonsai.Result
import com.example.budgetbonsai.ViewModelFactory
import com.example.budgetbonsai.data.model.UserModel
import com.example.budgetbonsai.data.local.UserPreference
import com.example.budgetbonsai.data.local.dataStore
import com.example.budgetbonsai.databinding.ActivityLoginBinding
import com.example.budgetbonsai.ui.MainActivity
import com.example.budgetbonsai.ui.MainViewModel
import com.example.budgetbonsai.ui.register.RegisterActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var userPreference: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
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

        binding.btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()
            viewModel.login(email, password).observe(this) {
                if (it != null) {
                    when (it) {
                        is Result.Error -> {
                            showLoading(false)
                            Toast.makeText(
                                this,
                                "Login Error",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        is Result.Loading -> {
                            showLoading(true)
                        }

                        is Result.Success -> {
                            showLoading(false)
                            val token = it.data.data?.token.toString()
                            val name = it.data.data?.name.toString()
                            viewModel.saveSession(
                                UserModel(
                                    name,
                                    token
                                )
                            )
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            Log.i("Token", token)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            }
        }

        binding.btnArrowback.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun checkSessionAndRedirect() {
        CoroutineScope(Dispatchers.Main).launch {
            if (userPreference.isSessionActive()) {
                goToMainActivity()
            }
        }
    }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}