package com.example.budgetbonsai.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.budgetbonsai.repository.Repository
import com.example.budgetbonsai.data.model.UserModel

class MainViewModel(private val repository: Repository) : ViewModel() {
    fun getLoginData(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
}