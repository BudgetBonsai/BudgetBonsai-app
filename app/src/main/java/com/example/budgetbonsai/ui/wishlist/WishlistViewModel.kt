package com.example.budgetbonsai.ui.wishlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.budgetbonsai.data.remote.response.WishlistItem
import com.example.budgetbonsai.repository.TransactionRepository
import com.example.budgetbonsai.repository.WishlistRepository
import com.example.budgetbonsai.ui.transaction.TransactionViewModel
import kotlinx.coroutines.launch

class WishlistViewModel(private val wishlistRepository: WishlistRepository) : ViewModel() {

    private val _wishlist = MutableLiveData<List<WishlistItem>>()
    val wishlist: LiveData<List<WishlistItem>> get() = _wishlist

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun fetchWishlist() {
        viewModelScope.launch {
            try {
                val response = wishlistRepository.getWishlist()
                if (response.error == true) {
                    _error.value = response.message!!
                } else {
                    _wishlist.value = response.data?.filterNotNull()
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    class WishlistViewModelFactory(private val repository: WishlistRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(WishlistViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return WishlistViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}