package com.example.budgetbonsai.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.budgetbonsai.R
import com.example.budgetbonsai.ViewModelFactory
import com.example.budgetbonsai.databinding.ActivityMainBinding
import com.example.budgetbonsai.ui.settings.SettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModelFactory = ViewModelFactory.getInstance(this)
    private val viewModel: MainViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Firebase keys
        val API_KEY_FIREBASE = "AIzaSyCAuxyfIF1dTHLoTClc6IqsSJfNalWjPIM"
        val APP_ID = "1:809522311403:android:6b08b0c500f202e6d3ceae"
        val APP_NAME = "budgetbonsai"

        //initialize firebase
        val firebaseOptions = FirebaseOptions.Builder()
            .setApiKey(API_KEY_FIREBASE)
            .setApplicationId(APP_ID).build()
        FirebaseApp.initializeApp(this, firebaseOptions, APP_NAME)

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestNotificationPermission()
        }
    }

    private fun requestNotificationPermission() {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED -> {
                // Permission is already granted
            }
            shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                // Show an explanation to the user why the permission is needed
            }
            else -> {
                // Directly request for required permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission is granted. Continue the action or workflow in your app.
        } else {
            // Permission is denied.
        }
    }
}