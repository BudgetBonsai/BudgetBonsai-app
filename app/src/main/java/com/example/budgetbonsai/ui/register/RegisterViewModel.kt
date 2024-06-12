package com.example.budgetbonsai.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.budgetbonsai.utils.Result
import com.example.budgetbonsai.data.Repository
import com.example.budgetbonsai.data.remote.response.RegisterResponse

class RegisterViewModel(private val repository: Repository) : ViewModel() {
    fun register(name: String, email: String, password: String
    ): LiveData<Result<RegisterResponse>> = repository.register(name, email, password)
}