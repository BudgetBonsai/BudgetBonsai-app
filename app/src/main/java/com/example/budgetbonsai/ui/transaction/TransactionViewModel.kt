package com.example.budgetbonsai.ui.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.budgetbonsai.Result
import com.example.budgetbonsai.data.Repository
import com.example.budgetbonsai.data.remote.response.DataItem
import com.example.budgetbonsai.data.remote.response.GetTransactionResponse
import kotlinx.coroutines.launch

class TransactionViewModel(private val repository: TransactionRepository) : ViewModel() {

    private val _transactionsLiveData = MutableLiveData<Result<List<DataItem>>>()
    val transactionsLiveData: LiveData<Result<List<DataItem>>> = _transactionsLiveData

    fun getTransactions() {
        _transactionsLiveData.value = Result.Loading // Set loading state
        viewModelScope.launch {
            try {
                val result = repository.getTransactions()
                _transactionsLiveData.postValue(result.value)
            } catch (e: Exception) {
                _transactionsLiveData.postValue(Result.Error("Network error occurred"))
            }
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