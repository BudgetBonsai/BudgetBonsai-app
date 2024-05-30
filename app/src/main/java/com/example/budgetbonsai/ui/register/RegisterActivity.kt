package com.example.budgetbonsai.ui.register

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.budgetbonsai.R
import com.example.budgetbonsai.databinding.ActivityRegisterBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

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
            onBackPressedDispatcher.onBackPressed()
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
}