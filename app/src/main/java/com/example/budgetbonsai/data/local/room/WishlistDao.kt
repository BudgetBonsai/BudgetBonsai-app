package com.example.budgetbonsai.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.example.budgetbonsai.data.model.WishlistTest

@Dao
interface WishlistDao {
    @Query("SELECT * FROM wishlist_table")
    fun getAllWishlist(): LiveData<List<WishlistTest>>

    @Insert
    fun insertWishlist(wishlist: WishlistTest)

    @Update
    fun updateWishlist(wishlist: WishlistTest)

    @Delete
    fun deleteWishlist(wishlist: WishlistTest)
}