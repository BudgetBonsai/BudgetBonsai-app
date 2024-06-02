package com.example.budgetbonsai.di

import android.content.Context
import com.example.budgetbonsai.data.Repository
import com.example.budgetbonsai.data.local.UserPreference
import com.example.budgetbonsai.data.local.dataStore
import com.example.budgetbonsai.data.remote.ApiConfig

object Injection {
    fun provideRepository(context: Context): Repository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService(pref)
//        val database = StoryDatabase.getDatabase(context)
        return Repository.getInstance(apiService, pref)
    }
}