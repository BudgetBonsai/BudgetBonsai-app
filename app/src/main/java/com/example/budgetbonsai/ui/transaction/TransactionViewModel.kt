package com.example.budgetbonsai.ui.transaction

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.budgetbonsai.utils.Result
import com.example.budgetbonsai.repository.TransactionRepository
import com.example.budgetbonsai.data.remote.response.DataItem
import com.example.budgetbonsai.data.remote.response.DateGet
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

class TransactionViewModel(private val repository: TransactionRepository) : ViewModel() {

    private val _transactionsLiveData = MutableLiveData<Result<List<DataItem>>>()
    val transactionsLiveData: LiveData<Result<List<DataItem>>> = _transactionsLiveData
    private val _totals = MutableLiveData<Pair<Double, Double>>()
    val totals: LiveData<Pair<Double, Double>> = _totals


    init {
        getTransactions()
        observeTransactions()
    }

    fun getTransactions() {
        _transactionsLiveData.value = Result.Loading
        viewModelScope.launch {
            try {
                val result = repository.getTransactions()
                _transactionsLiveData.postValue(result.value)
            } catch (e: Exception) {
                _transactionsLiveData.postValue(Result.Error("Network error occurred"))
            }
        }
    }

    private fun observeTransactions() {
        _transactionsLiveData.observeForever { result ->
            when (result) {
                is Result.Success -> {
                    val transactions = filterTransactionsByCurrentMonth(result.data ?: emptyList())
                    Log.d("TransactionViewModel", "Filtered Transactions: $transactions")
                    val totals = repository.calculateTotals(transactions)
                    Log.d("TransactionViewModel", "Calculated Totals: $totals")
                    _totals.value = totals
                }
                is Result.Error -> {
                    Log.e("TransactionViewModel", "Error: ${result.error}")
                    _totals.value = Pair(0.0, 0.0)
                }
                else -> {
                    // do nothing
                }
            }
        }
    }

    fun getTotals() {
        viewModelScope.launch {
            val result = repository.getTotals()
            _totals.postValue(result.value)
        }
    }

    private fun getCurrentMonth(): Int {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.MONTH) + 1 // Calendar.MONTH is zero-based
    }

    private fun getCurrentYear(): Int {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.YEAR)
    }

    private fun convertDateGetToDate(dateGet: DateGet): Date {
        return Date((dateGet.seconds?.toLong() ?: 0) * 1000)
    }

    private fun filterTransactionsByCurrentMonth(transactions: List<DataItem>): List<DataItem> {
        val currentMonth = getCurrentMonth()
        val currentYear = getCurrentYear()
        val filteredTransactions = transactions.filter { transaction ->
            transaction.date?.let {
                val transactionDate = convertDateGetToDate(it)
                val calendar = Calendar.getInstance()
                calendar.time = transactionDate
                val transactionMonth = calendar.get(Calendar.MONTH) + 1
                val transactionYear = calendar.get(Calendar.YEAR)
                transactionMonth == currentMonth && transactionYear == currentYear
            } ?: false
        }
        Log.d("TransactionViewModel", "Filtered Transactions by Current Month: $filteredTransactions")
        return filteredTransactions
    }

}

class TransactionViewModelFactory(private val repository: TransactionRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransactionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TransactionViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}