package com.example.budgetbonsai.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.example.budgetbonsai.data.local.UserPreference
import com.example.budgetbonsai.data.remote.ApiService
import retrofit2.HttpException
import com.example.budgetbonsai.Result
import com.example.budgetbonsai.data.model.UserModel
import com.example.budgetbonsai.data.remote.response.DataItem
import com.example.budgetbonsai.data.remote.response.GetTransactionResponse
import com.example.budgetbonsai.data.remote.response.LoginResponse
import com.example.budgetbonsai.data.remote.response.RegisterResponse
import kotlinx.coroutines.flow.Flow

class Repository private constructor(
     val apiService: ApiService,
     val userPreference: UserPreference,
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

    fun getTransactions(): LiveData<Result<GetTransactionResponse>> = liveData {
        emit(Result.Loading)

        try {
            val client = apiService.getTransactions()
            if (client.error == false) {
                emit(Result.Success(client))
            } else {
                Log.e("GetStoriesError", "${client.message}")
                emit(Result.Error(client.message.toString()))
            }
        } catch (e: HttpException) {
            Log.e("GetStoriesHTTP", "${e.message}")
            emit(Result.Error(e.message().toString()))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun logout() {
        userPreference.logout()
    }

//    suspend fun getFinancialRecords(email: String): List<FinancialRecord> {
//        val recordsFromApi = apiService.getFinancialRecords()
//        recordsFromApi.forEach { record ->
//            db.financialRecordDao().insertRecord(record)
//        }
//        return db.financialRecordDao().getRecords(email)
//    }

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