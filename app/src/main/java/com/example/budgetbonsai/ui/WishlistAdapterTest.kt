package com.example.budgetbonsai.ui

import android.app.Application
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.budgetbonsai.R
import com.example.budgetbonsai.Wishlist
import com.example.budgetbonsai.WishlistViewHolder
import com.example.budgetbonsai.WishlistViewHolderTest
import com.example.budgetbonsai.data.model.WishlistTest
import com.example.budgetbonsai.ui.wishlist.WishlistViewModel

class WishlistAdapterTest(
    private val context: Context,
    private val wishlist: List<WishlistTest>,
    private val onDepositClick: (WishlistTest) -> Unit,
    private val onWithdrawClick: (WishlistTest) -> Unit,
    private val onDeleteClick: (WishlistTest) -> Unit,
    private val onEditClick: (WishlistTest) -> Unit
) : RecyclerView.Adapter<WishlistViewHolderTest>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishlistViewHolderTest {
        val view = LayoutInflater.from(context).inflate(R.layout.item_wishlist, parent, false)
        return WishlistViewHolderTest(view)
    }

    override fun onBindViewHolder(holder: WishlistViewHolderTest, position: Int) {
        val item = wishlist[position]
        holder.bind(item, onDepositClick, onWithdrawClick, onDeleteClick, onEditClick)
    }

    override fun getItemCount(): Int {
        return wishlist.size
    }
}