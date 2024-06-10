package com.example.budgetbonsai

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WishlistAdapter(private val wishlist: List<Wishlist>, private val itemClickListener: OnItemClickListener) : RecyclerView.Adapter<WishlistViewHolder>() {

    interface OnItemClickListener {
        fun onDepositClick(item: Wishlist)
        fun onWithdrawClick(item: Wishlist)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishlistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_wishlist, parent, false)
        return WishlistViewHolder(view)
    }

    override fun onBindViewHolder(holder: WishlistViewHolder, position: Int) {
        val item = wishlist[position]
        holder.bind(item)
        holder.setOnClickListener {
            when (it) {
                R.id.btn_deposit -> itemClickListener.onDepositClick(item)
                R.id.btn_withdraw -> itemClickListener.onWithdrawClick(item)
            }
        }
    }

    override fun getItemCount(): Int {
        return wishlist.size
    }
}