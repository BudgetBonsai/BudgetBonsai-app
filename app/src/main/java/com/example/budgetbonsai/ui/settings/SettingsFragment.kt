package com.example.budgetbonsai.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.budgetbonsai.R
import com.example.budgetbonsai.ViewModelFactory
import com.example.budgetbonsai.data.Repository
import com.example.budgetbonsai.data.local.UserPreference
import com.example.budgetbonsai.data.local.dataStore
import com.example.budgetbonsai.data.remote.ApiConfig
import com.example.budgetbonsai.ui.landing.LandingActivity

class SettingsFragment : Fragment() {

    private lateinit var viewModel: SettingsViewModel
    private lateinit var userPreference: UserPreference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        userPreference = UserPreference.getInstance(requireContext().dataStore)
        val repository = Repository(ApiConfig.getApiService(userPreference), userPreference)
        val viewModelFactory = ViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(SettingsViewModel::class.java)

        view.findViewById<Button>(R.id.btn_signout).setOnClickListener {
            viewModel.logout()
            val intent = Intent(requireContext(), LandingActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        return view
    }

}