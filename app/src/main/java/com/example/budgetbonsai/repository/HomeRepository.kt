package com.example.budgetbonsai.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.budgetbonsai.data.local.UserPreference
import com.example.budgetbonsai.data.model.UserModel
import com.example.budgetbonsai.utils.Result
import com.example.budgetbonsai.data.remote.ApiService
import com.example.budgetbonsai.data.remote.PredictApiService
import com.example.budgetbonsai.data.remote.response.PredictResponse2
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class HomeRepository (
    val apiService: PredictApiService,
    val userPreference: UserPreference,
){
    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

//    suspend fun getPredictResponse(token: String): PredictResponse2 {
//        return apiService.getPredict(token)
//    }

    suspend fun getPredict(): LiveData<Result<PredictResponse2>> {
        val resultLiveData = MutableLiveData<Result<PredictResponse2>>()
        try {
            val response = apiService.getPredict()
            resultLiveData.value = Result.Success(response)
        } catch (e: Exception) {
            resultLiveData.value = Result.Error("Network error occurred")
        }
        return resultLiveData
    }

    suspend fun getUserToken(): String {
        return userPreference.getSession().firstOrNull()?.token ?: ""
    }

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }
}