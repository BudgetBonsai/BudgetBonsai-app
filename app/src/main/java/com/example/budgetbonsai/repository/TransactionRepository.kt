package com.example.budgetbonsai.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.budgetbonsai.data.local.UserPreference
import com.example.budgetbonsai.data.remote.ApiService
import com.example.budgetbonsai.data.remote.response.DataItem
import com.example.budgetbonsai.utils.Result

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

    fun calculateTotals(transactions: List<DataItem>): Pair<Double, Double> {
        var totalIncome = 0.0
        var totalExpense = 0.0

        for (transaction in transactions) {
            val amount = transaction.amount?.toDouble() ?: 0.0
            if (transaction.type?.lowercase() == "income") {
                totalIncome += amount
            } else {
                totalExpense += amount
            }
        }

        Log.d("TransactionRepository", "Total Income: $totalIncome, Total Expense: $totalExpense")
        return Pair(totalIncome, totalExpense)
    }

//    suspend fun getTotals(): LiveData<Pair<Double, Double>> {
//        val totalLiveData = MutableLiveData<Pair<Double, Double>>()
//        getTransactions().observeForever { result ->
//            if (result is Result.Success) {
//                val transactions = result.data
//                val totals = calculateTotals(transactions)
//                totalLiveData.value = totals
//            }
//        }
//        return totalLiveData
//    }

    suspend fun getTotals(): LiveData<Pair<Double, Double>> {
        val totalLiveData = MutableLiveData<Pair<Double, Double>>()
        val transactionResult = getTransactions().value

        when (transactionResult) {
            is Result.Success -> {
                val transactions = transactionResult.data
                val totals = calculateTotals(transactions)
                totalLiveData.value = totals
            }
            is Result.Error -> {
                totalLiveData.value = Pair(0.0, 0.0)
            }
            else -> {
                // Handle other cases if needed
            }
        }

        return totalLiveData
    }
}