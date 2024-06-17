package com.example.budgetbonsai.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgetbonsai.repository.Repository
import kotlinx.coroutines.launch

class SettingsViewModel(private val repository: Repository) : ViewModel() {
    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}