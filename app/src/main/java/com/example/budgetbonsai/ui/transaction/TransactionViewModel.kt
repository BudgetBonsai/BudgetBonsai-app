package com.example.budgetbonsai.ui.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.budgetbonsai.utils.Result
import com.example.budgetbonsai.repository.TransactionRepository
import com.example.budgetbonsai.data.remote.response.DataItem
import kotlinx.coroutines.launch

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
                    val transactions = result.data
                    val totals = repository.calculateTotals(transactions)
                    _totals.value = totals
                }
                is Result.Error -> {
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