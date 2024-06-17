package com.example.budgetbonsai.repository

import com.example.budgetbonsai.data.remote.ApiService
import com.example.budgetbonsai.data.remote.response.WishlistResponse

class WishlistRepository(private val apiService: ApiService) {

    suspend fun getWishlist(): WishlistResponse {
        return apiService.getWishlist()
    }
}