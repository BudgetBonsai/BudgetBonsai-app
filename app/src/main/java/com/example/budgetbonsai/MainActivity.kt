package com.example.budgetbonsai

import android.os.Bundle
import android.text.TextUtils.replace
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNavView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNavView.setupWithNavController(navController)

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
                else -> false
            }
        }
    }
}