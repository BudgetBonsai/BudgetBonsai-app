package com.example.budgetbonsai.data

import com.example.budgetbonsai.Wishlist
import com.example.budgetbonsai.data.local.room.WishlistDao
import com.example.budgetbonsai.data.model.WishlistTest

class WishlistRepository(private val wishlistDao: WishlistDao) {
    val allWishlist = wishlistDao.getAllWishlist()

    suspend fun insertWishlist(wishlist: WishlistTest) {
        wishlistDao.insertWishlist(wishlist)
    }

    suspend fun updateWishlist(wishlist: WishlistTest) {
        wishlistDao.updateWishlist(wishlist)
    }

    suspend fun deleteWishlist(wishlist: WishlistTest) {
        wishlistDao.deleteWishlist(wishlist)
    }
}