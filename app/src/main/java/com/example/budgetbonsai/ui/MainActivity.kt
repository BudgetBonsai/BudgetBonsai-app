package com.example.budgetbonsai.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.budgetbonsai.R
import com.example.budgetbonsai.ViewModelFactory
import com.example.budgetbonsai.databinding.ActivityMainBinding
import com.example.budgetbonsai.ui.settings.SettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModelFactory = ViewModelFactory.getInstance(this)
    private val viewModel: MainViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNavView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNavView.setupWithNavController(navController)

        viewModel.getLoginData().observe(this) {
            val loginState = it.isLogin
            val token = it.token
            Log.d("LOGINSTATE", loginState.toString())
            Log.d("TOKENSTATE", token.toString())
        }

        NavigationBarView.OnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.homeFragment -> {
                    // Respond to navigation item 1 click
                    true
                }
                R.id.transactionFragment -> {
                    // Respond to navigation item 2 click
                    true
                }
                R.id.addFragment -> {
                    // Respond to navigation item 3 click
                    true
                }
                R.id.wishlistFragment -> {
                    // Respond to navigation item 4 click
                    true
                }

                R.id.settingsFragment -> {

                    true
                }
                else -> false
            }
        }
    }
}