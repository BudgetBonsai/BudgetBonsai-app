package com.example.budgetbonsai.ui.wishlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgetbonsai.Wishlist
import com.example.budgetbonsai.data.WishlistRepository
import com.example.budgetbonsai.data.model.WishlistTest
import kotlinx.coroutines.launch

class WishlistViewModel(private val repository: WishlistRepository) : ViewModel() {

    private val _allWishlist = MutableLiveData<List<WishlistTest>>()
    val allWishlist: LiveData<List<WishlistTest>>
        get() = _allWishlist

//    val allWishlist: List<WishlistTest> = repository.allWishlist

    init {
        // Initialize the wishlist with dummy data or fetch from repository
        _allWishlist.value = listOf(
            WishlistTest(1, "laptop", 100.0, 50.0,"2021-12-31")
        )
    }


    fun insert(wishlist: WishlistTest) = viewModelScope.launch {
        repository.insertWishlist(wishlist)
    }

    fun update(wishlist: WishlistTest) = viewModelScope.launch {
        repository.updateWishlist(wishlist)
    }

    fun delete(wishlist: WishlistTest) = viewModelScope.launch {
        repository.deleteWishlist(wishlist)
    }

}