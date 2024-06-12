package com.example.budgetbonsai.ui.splashscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {
    private val _isTimerFinished = MutableLiveData(false)
    val isTimerFinished: LiveData<Boolean> get() = _isTimerFinished

    fun startSplashTimer() {
        viewModelScope.launch {
            delay(3000) // 3 detik
            _isTimerFinished.value = true
        }
    }
}