package com.example.budgetbonsai.ui.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.budgetbonsai.data.local.UserPreference
import com.example.budgetbonsai.data.remote.ApiService
import com.example.budgetbonsai.data.remote.response.DataItem
import com.example.budgetbonsai.Result

class TransactionRepository(private val apiService: ApiService, private val userPreference: UserPreference) {

    suspend fun getTransactions(): LiveData<Result<List<DataItem>>> {
        val resultLiveData = MutableLiveData<Result<List<DataItem>>>()
        try {
            val response = apiService.getTransactions()
            if (response.error == false) {
                val data = response.data?.filterNotNull() ?: emptyList()
                resultLiveData.value = Result.Success(data)
            } else {
                val errorMessage = response.message ?: "Error occurred"
                resultLiveData.value = Result.Error(errorMessage)
            }
        } catch (e: Exception) {
            resultLiveData.value = Result.Error("Network error occurred")
        }
        return resultLiveData
    }
}