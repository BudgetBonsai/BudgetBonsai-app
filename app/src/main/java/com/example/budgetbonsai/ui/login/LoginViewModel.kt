package com.example.budgetbonsai.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgetbonsai.Result
import com.example.budgetbonsai.data.Repository
import com.example.budgetbonsai.data.model.UserModel
import com.example.budgetbonsai.data.remote.response.LoginResponsee
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: Repository) : ViewModel() {
    fun login(email: String, password: String): LiveData<Result<LoginResponsee>> =
        repository.login(email, password)

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}