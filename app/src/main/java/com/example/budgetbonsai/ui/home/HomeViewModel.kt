package com.example.budgetbonsai.ui.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.budgetbonsai.data.model.UserModel
import com.example.budgetbonsai.data.remote.response.PredictResponse2
import com.example.budgetbonsai.repository.HomeRepository
import com.example.budgetbonsai.utils.Result
import com.example.budgetbonsai.repository.Repository
import com.example.budgetbonsai.repository.WishlistRepository
import com.example.budgetbonsai.ui.wishlist.WishlistViewModel
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: HomeRepository) : ViewModel() {
    fun getLoginData(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    private val _growthMessageLiveData = MutableLiveData<Result<PredictResponse2>>()
    val growthMessageLiveData: LiveData<Result<PredictResponse2>>
        get() = _growthMessageLiveData
    private val _predictLiveData = MutableLiveData<Result<PredictResponse2>>()
    val predictLiveData: LiveData<Result<PredictResponse2>>
        get() = _predictLiveData

    init {
        fetchPredict()
    }

    fun fetchPredict() {
        _predictLiveData.value = Result.Loading
        viewModelScope.launch {
            try {
                val result = repository.getPredict()
                _predictLiveData.postValue(result.value)
            } catch (e: Exception) {
                _predictLiveData.postValue(Result.Error("Network error occurred"))
            }
        }
    }

//    fun fetchGrowthMessage() {
//        viewModelScope.launch {
//            _growthMessageLiveData.value = Result.Loading
//            try {
//                val token = repository.getUserToken()
//                val response = repository.getPredictResponse("Bearer $token")
//                _growthMessageLiveData.value = Result.Success(response)
//            } catch (e: Exception) {
//                _growthMessageLiveData.value = Result.Error(e.toString())
//            }
//        }
//    }

    class HomeViewModelFactory(private val repository: HomeRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return HomeViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}