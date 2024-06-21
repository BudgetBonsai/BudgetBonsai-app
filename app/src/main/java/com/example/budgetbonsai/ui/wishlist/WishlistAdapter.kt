package com.example.budgetbonsai.ui.wishlist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.budgetbonsai.R
import com.example.budgetbonsai.data.remote.response.WishlistItem
import com.example.budgetbonsai.databinding.ItemWishlistBinding

class WishlistAdapter(private val context: Context, private var wishlist: List<WishlistItem>, private val onDeleteClick: (WishlistItem) -> Unit,
                      private val onEditClick: (WishlistItem) -> Unit, private val onDepositClick: (WishlistItem) -> Unit) : RecyclerView.Adapter<WishlistViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishlistViewHolder {
        val binding = ItemWishlistBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return WishlistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WishlistViewHolder, position: Int) {
        holder.btnDelWishlist.setOnClickListener {
            onDeleteClick(wishlist[position])
        }
        holder.btnEditWishlist.setOnClickListener {
            onEditClick(wishlist[position])
        }
holder.setOnClickListener {
            onDepositClick(wishlist[position])
        }

        holder.bind(wishlist[position])
    }

    fun setItems(items: List<WishlistItem>) {
        wishlist = items
        notifyDataSetChanged()
    }

    private fun showDepositDialog() {
        val dialog = DepositFragment()
        if (context is FragmentActivity) {
            dialog.show((context as FragmentActivity).supportFragmentManager, DepositFragment.TAG)
        } else {
            throw ClassCastException("Context must be an instance of FragmentActivity")
        }
    }

    override fun getItemCount(): Int {
        return wishlist.size
    }
}