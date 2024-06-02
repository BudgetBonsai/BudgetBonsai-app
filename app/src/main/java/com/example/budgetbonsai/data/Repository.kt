package com.example.budgetbonsai.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.budgetbonsai.data.local.UserPreference
import com.example.budgetbonsai.data.remote.ApiService
import com.example.budgetbonsai.data.remote.response.LoginResponse
import com.example.budgetbonsai.data.remote.response.RegisterResponse
import retrofit2.HttpException
import com.example.budgetbonsai.Result
import kotlinx.coroutines.flow.Flow

class Repository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
){

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun register(name: String, email: String, password: String
    ): LiveData<Result<RegisterResponse>> = liveData {
        emit(Result.Loading)
        try {
            val client = apiService.register(name, email, password)
            emit(Result.Success(client))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
            Log.e("PostRegisterHttp", e.message.toString())
        }
    }

    fun login(email: String, password: String
    ): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
            try {
                val client = apiService.login(email, password)
                emit(Result.Success(client))
            } catch (e: HttpException) {
                Log.e("PostLoginHttp", "${e.message()}")
                emit(Result.Error(e.message().toString()))
            }
    }

    companion object {
        @Volatile
        private var instance: Repository? = null

        fun getInstance(apiService: ApiService, userPreference: UserPreference): Repository {
            return instance ?: synchronized(this) {
                instance ?: Repository(apiService, userPreference).also { instance = it }
            }
        }
    }
}