package com.example.budgetbonsai.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.budgetbonsai.R
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
                R.id.wishlistFragment -> {
                    // Respond to navigation item 4 click
                    true
                }

                R.id.settingsFragment -> {
                    // Respond to navigation item 4 click
                    true
                }
                else -> false
            }
        }
    }
}