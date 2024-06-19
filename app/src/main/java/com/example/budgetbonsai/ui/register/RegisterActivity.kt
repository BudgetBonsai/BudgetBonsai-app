package com.example.budgetbonsai.ui.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.budgetbonsai.R
import com.example.budgetbonsai.utils.Result
import com.example.budgetbonsai.ViewModelFactory
import com.example.budgetbonsai.databinding.ActivityRegisterBinding
import com.example.budgetbonsai.ui.landing.LandingActivity
import com.example.budgetbonsai.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnArrowback.setOnClickListener {
            val intent = Intent(this, LandingActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnGotologin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        val viewModelFactory = ViewModelFactory.getInstance(this@RegisterActivity)
        val viewModel: RegisterViewModel by viewModels { viewModelFactory}

        binding.btnRegister.setOnClickListener {
            viewModel.register(
                binding.edRegisterName.text.toString(),
                binding.edRegisterEmail.text.toString(),
                binding.edRegisterPassword.text.toString()
            ).observe(this) {
                if (it != null) {
                    when (it) {
                        is Result.Loading -> {
                            showLoading(true)
                        }
                        is Result.Error -> {
                            showLoading(false)
                            Toast.makeText(this, "Error Regist", Toast.LENGTH_SHORT).show()
                        }
                        is Result.Success -> {
                            showLoading(false)
                            Toast.makeText(this, "Regist success", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            }
        }
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

        //Dialog
//        binding.loginButton.setOnClickListener {
//            MaterialAlertDialogBuilder(this)
//                .setTitle("Register")
//                .setMessage("Registering...")
//                .setPositiveButton("OK") { dialog, which ->
//                    dialog.dismiss()
//                }
//                .show()
//        }
    }
